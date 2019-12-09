package com.robot.center.dispatch;

import com.alibaba.fastjson.JSON;
import com.bbin.common.util.ThreadLocalUtils;
import com.robot.center.constant.RobotConsts;
import com.robot.center.execute.TaskWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.code.entity.TenantRobotRecord;
import com.robot.code.service.ITenantRobotRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Set;

/**
 * Created by mrt on 10/31/2019 12:33 PM
 * 功能：任务队列的维护
 * 问题1：
 *      队列里面的任务只能一个一个执行，如果张三正在等待执行（按会员账号限制），阻挡了新来的李四的执行
 * 解决：
 *      进入队列加排序时间戳（任务该在哪个时间戳之后执行），既能排序又可知执行时间
 * 问题2：
 *      机器人项目重启后或机器人全部挂掉后，任务队列长期积压问题，时间戳不准确（可能全部过期）
 * 解决：
 *      使用限制时间进行阻挡
 */
@Slf4j
@Service
public class TaskPoolImpl implements ITaskPool {
    private static final String CLASS_NAME = "任务池";
    private static final String CACHE_TASK_QUEUE = RobotConsts.ROBOT_PROJECT_PERFIX + "TASK:QUEUE:";
    private static final String CACHE_TASK_LIMIT = RobotConsts.ROBOT_PROJECT_PERFIX + "TASK:TIME_LIMIT:";
    private static final String EXTERNAL_NO = RobotConsts.ROBOT_PROJECT_PERFIX + "EXTERNAL_NO:";
    @Resource
    private RedisTemplate<String,TaskWrapper> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TaskTimeCalculate timeCalculate;
    @Autowired
    private Reactor reactor;
    @Autowired
    private ITenantRobotRecordService robotRecordService;


    // 同一会员高并发会导致时间戳计算问题
    @Override
    public void taskAdd(TaskWrapper taskWrapper, String externalNo) {
        if (StringUtils.isNotBlank(externalNo)) {
            boolean isRedo = isRedo(externalNo);
            if (isRedo) {
                log.info("该外部订单号已经存在,将不执行,externalNo:{},功能参数:{}", externalNo, JSON.toJSONString(taskWrapper));
                return;
            }
        }

        // 时间的计算
        long timeStamp = timeCalculate.calcTaskTimeStamp(taskWrapper);
        Boolean isAdd = redisTemplate.opsForZSet().add(createCacheQueueKey(), taskWrapper, timeStamp);
        if (isAdd) {
            reactor.registerEvents(createRegisterBody());
            log.info("{}:任务入队成功:{},timeStamp:{}", CLASS_NAME, JSON.toJSONString(taskWrapper), timeStamp);
        } else {
            log.info("{}:任务入队失败:{},timeStamp:{}", CLASS_NAME, JSON.toJSONString(taskWrapper), timeStamp);
        }
    }

    @Override
    public long size() {
        return redisTemplate.opsForZSet().zCard(createCacheQueueKey());
    }

    // 本方法是线程安全的，无并发问题
    @Override
    public TaskWrapper taskGet() {
        TaskWrapper taskWrapper = taskSkim();
        if (null == taskWrapper) {
            return null;
        }
        // 对时间进行限制
        Boolean isSet = stringRedisTemplate.opsForValue().setIfAbsent(CACHE_TASK_LIMIT + taskWrapper.getWaitField(), "", taskWrapper.getWaitTime());
        if (!isSet) {
            return null;
        }

        // 注意：remove返回被移除的元素数量
        Long removeNum = redisTemplate.opsForZSet().remove(createCacheQueueKey(), taskWrapper);
        if (removeNum <= 0) {
            return null;
        }
        log.info("{}:任务出队成功:{}", CLASS_NAME, JSON.toJSONString(taskWrapper));
        return taskWrapper;
    }

    @Override
    public TaskWrapper taskSkim() {
        String cacheQueueKey = createCacheQueueKey();
        Set<TaskWrapper> range = redisTemplate.opsForZSet().rangeByScore(cacheQueueKey, 0D, System.currentTimeMillis(), 0L, 1L);
        return 0 == range.size() ? null : range.iterator().next();
    }


    protected boolean isRedo(String externalNo) {
        // redis检查
        String cacheKey = createExteralNoCacheKey(externalNo);
        Boolean isSave = stringRedisTemplate.opsForValue().setIfAbsent(cacheKey, "", Duration.ofDays(10));
        if (!isSave) {
            log.info("redis:该外部订单已经存在：{}", externalNo);
            return true;
        }

        // mysql检查
        TenantRobotRecord robotRecord=robotRecordService.getRecordByExternalOrderNo(externalNo);
        if (null != robotRecord) {
            log.info("mysql:该外部订单已经存在：{}", externalNo);
            return true;
        }
        return false;
    }

    // CACHE：创建任务缓冲队列
    private static String createCacheQueueKey() {
        return new StringBuilder(30)
                .append(CACHE_TASK_QUEUE)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction())
                .toString();
    }

    private RegisterBody createRegisterBody() {
        return new RegisterBody(
                RobotThreadLocalUtils.getTenantId(),
                RobotThreadLocalUtils.getChannelId(),
                RobotThreadLocalUtils.getPlatformId(),
                RobotThreadLocalUtils.getFunction()
        );
    }


    private String createExteralNoCacheKey(String externalNo) {
        Long tenantId = ThreadLocalUtils.getTenantIdOption().get();
        Long channelId = ThreadLocalUtils.getChannelIdOption().get();
        Long platformId = RobotConsts.PLATFORM_ID.BBIN;
        Long functionCode = RobotConsts.FUNCTION_CODE.ACTIVITY;
        return new StringBuilder(30)
                .append(EXTERNAL_NO)
                .append(tenantId).append(":")
                .append(channelId).append(":")
                .append(platformId).append(":")
                .append(functionCode).append(":")
                .append(externalNo)
                .toString();
    }
}
