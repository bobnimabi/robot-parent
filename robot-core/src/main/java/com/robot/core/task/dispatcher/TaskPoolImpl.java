package com.robot.core.task.dispatcher;

import com.alibaba.fastjson.JSON;
import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
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

    /**
     * redis：任务队列名称前缀
     */
    private static final String CACHE_TASK_QUEUE = RedisConsts.PROJECT + "TASK:QUEUE:";

    @Resource(name = "redisTemplate")
    private RedisTemplate<String,TaskWrapper> taskRedis;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ISelector reactor;

    @Override
    public void taskAdd(TaskWrapper taskWrapper) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        long timeStamp = LimitFieldHelper.calc(taskWrapper, redis);
        Boolean isAdd = taskRedis.opsForZSet().add(createCacheQueueKey(), taskWrapper, timeStamp);
        if (isAdd) {
            reactor.registerEvents(createRegisterBody());
            log.info("{}:任务入队成功:{},timeStamp:{}", CLASS_NAME, JSON.toJSONString(taskWrapper), timeStamp);
        } else {
            log.info("{}:任务入队失败:{},timeStamp:{}", CLASS_NAME, JSON.toJSONString(taskWrapper), timeStamp);
        }
    }

    @Override
    public long size() {
        return taskRedis.opsForZSet().zCard(createCacheQueueKey());
    }

    @Override
    public TaskWrapper taskGet() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TaskWrapper taskWrapper = taskSkim();
        if (null == taskWrapper) {
            return null;
        }
        // 防止过去一段时间任务堆积
        boolean hasLimit = LimitFieldHelper.hasLimit(taskWrapper, redis);
        if (hasLimit) {
            return null;
        }

        /**
         * 注意：remove返回被移除的元素数量
         * 分布式下删除成功的得到TaskWrapper
          */
        Long removeNum = taskRedis.opsForZSet().remove(createCacheQueueKey(), taskWrapper);
        if (removeNum <= 0) {
            return null;
        }
        log.info("{}:任务出队成功:{}", CLASS_NAME, JSON.toJSONString(taskWrapper));
        return taskWrapper;
    }

    @Override
    public TaskWrapper taskSkim() {
        String cacheQueueKey = createCacheQueueKey();
        Set<TaskWrapper> range = taskRedis.opsForZSet().rangeByScore(cacheQueueKey, 0D, System.currentTimeMillis(), 0L, 1L);
        return 0 == range.size() ? null : range.iterator().next();
    }

    // CACHE：创建任务缓冲队列
    private static String createCacheQueueKey() {
        return new StringBuilder(30)
                .append(CACHE_TASK_QUEUE)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction())
                .toString();
    }

    private RegisterBody createRegisterBody() {
        return new RegisterBody(
                TContext.getTenantId(),
                TContext.getChannelId(),
                TContext.getPlatformId(),
                TContext.getFunction()
        );
    }
}
