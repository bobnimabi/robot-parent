package com.robot.core.task.dispatcher;

import com.alibaba.fastjson.JSON;
import com.robot.code.entity.AsyncRequestConfig;
import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import com.robot.core.function.base.ParamWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
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

    @Resource
    private RedisTemplate<String,TaskWrapper> taskRedis;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private Reactor reactor;

    /**
     * 缓存反射对象的容器，提升性能
     * 注意：正常情况下，就一个打款需要
     */
    private Map<String, MethodInvoker> map = new HashMap<>(1);

    @Override
    public void taskAdd(TaskWrapper taskWrapper, String externalNo) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        AsyncRequestConfig config = taskWrapper.getConfig();
        ParamWrapper paramWrapper = taskWrapper.getParamWrapper();


        long timeStamp = LimitFieldHelper.calc(Duration.ofSeconds(config.getFieldTime()), getWaitName(config, paramWrapper), redis);
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
    public TaskWrapper taskGet() {
        TaskWrapper taskWrapper = taskSkim();
        if (null == taskWrapper) {
            return null;
        }
        // 防止过去一段时间任务堆积
        boolean hasLimit = LimitFieldHelper.hasLimit(taskWrapper.getWaitTime(), taskWrapper.getWaitField(), redis);
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

    /**
     * 根据反射获取Field
     * @param config
     * @param paramWrapper
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String getWaitName(AsyncRequestConfig config, ParamWrapper paramWrapper) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        String field = config.getField();
        String targetMethodStr = "get" + toUpperFirstChar(field);
        String canonicalName = paramWrapper.getObj().getClass().getCanonicalName();
        String key = new StringBuilder(canonicalName.length() + targetMethodStr.length() + 1).append(canonicalName).append(".").append(targetMethodStr).toString();
        MethodInvoker invoker = map.get(key);
        if (null == invoker) {
            invoker = new MethodInvoker();
            invoker.setTargetObject(paramWrapper.getObj());
            invoker.setTargetMethod(targetMethodStr);
            if (!invoker.isPrepared()) {
                invoker.prepare();
            }
            map.put(key, invoker);
        }
        String value = (String) invoker.invoke();
        Assert.hasText(value, "反射获取：" + field + " 没有值");
        return new StringBuilder(field.length() + value.length() + 1).append(field).append(":").append(value).toString();
    }

    /**
     * 首字母大写
     * @param string
     * @return
     */
    private String toUpperFirstChar(String string) {
        char[] chars = string.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] -= 32;
            return String.valueOf(chars);
        }
        return string;
    }
}
