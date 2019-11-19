package com.robot.center.dispatch;

import com.alibaba.fastjson.JSON;
import com.robot.center.constant.RobotConsts;
import com.robot.center.execute.TaskWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by mrt on 10/31/2019 12:33 PM
 * 功能：任务队列的维护
 */
@Slf4j
@Service
public class TaskPoolImpl implements ITaskPool {
    private static final String CLASS_NAME = "任务池";
    private static final String CACHE_TASK_QUEUE = RobotConsts.ROBOT_PROJECT_PERFIX + "TASK:QUEUE:";

    @Resource
    private RedisTemplate<String,TaskWrapper> redisTemplate;
    @Autowired
    private TaskTimeCalculate timeCalculate;
    @Autowired
    private Reactor reactor;

    // 同一会员高并发会导致时间戳计算问题
    @Override
    public void taskAdd(TaskWrapper taskWrapper) {
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
        String cacheQueueKey = createCacheQueueKey();
        Set<TaskWrapper> range = redisTemplate.opsForZSet().rangeByScore(cacheQueueKey, 0D, System.currentTimeMillis(), 0L, 1L);
        if (0 == range.size()) {
            return null;
        }
        TaskWrapper taskWrapper = range.iterator().next();
        // 注意：remove返回被移除的元素数量
        Long removeNum = redisTemplate.opsForZSet().remove(cacheQueueKey, taskWrapper);
        if (removeNum <= 0) {
            return null;
        }
        log.info("{}:任务出队成功:{}", CLASS_NAME, JSON.toJSONString(taskWrapper));
        return taskWrapper;
    }

    public TaskWrapper taskSkim() {
        String cacheQueueKey = createCacheQueueKey();
        Set<TaskWrapper> range = redisTemplate.opsForZSet().rangeByScore(cacheQueueKey, 0D, System.currentTimeMillis(), 0L, 1L);
        return range.iterator().next();
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
}
