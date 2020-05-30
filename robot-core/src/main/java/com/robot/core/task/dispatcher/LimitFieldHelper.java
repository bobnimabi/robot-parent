package com.robot.core.task.dispatcher;

import com.robot.code.entity.AsyncRequestConfig;
import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import com.robot.core.function.base.ParamWrapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mrt
 * @Date 2020/5/28 17:23
 * @Version 2.0
 */
@Service
public class LimitFieldHelper {

    /**
     * redis：域时间戳计算前缀
     */
    private static final String CACHE_TIME_LIMIT = RedisConsts.PROJECT + "TASK:FIELD:TIMESTAMP:";

    /**
     * redis:域限制时间段前缀
     */
    private static final String CACHE_TASK_INTERAL = RedisConsts.PROJECT + "TASK:FIELD:INTERAL:";

    /**
     * 缓存反射对象的容器，提升性能
     * 注意：正常情况下，就一个打款需要
     */
    private static Map<String, MethodInvoker> map = new HashMap<>(1);

    /**
     * 入队时：计算分数（预计出队时间戳）
     * @param taskWrapper
     * @param redis
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static long calc(TaskWrapper taskWrapper, StringRedisTemplate redis) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        AsyncRequestConfig config = taskWrapper.getConfig();
        ParamWrapper paramWrapper = taskWrapper.getParamWrapper();
        long timeStamp = System.currentTimeMillis();
        if (null != config && !StringUtils.isEmpty(config.getField()) && null != config.getFieldTime() && config.getFieldTime() > 0) {
            timeStamp = calc(getWaitName(config, paramWrapper), Duration.ofSeconds(config.getFieldTime()), redis);
        }
        return timeStamp;
    }


    /**
     * 任务出队列时查看是否有时间阻挡
     * 防止：长期任务堆积，导致出队时间戳失效问题
     * @param taskWrapper
     * @return
     */
    public static boolean hasLimit(TaskWrapper taskWrapper,StringRedisTemplate redis) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        AsyncRequestConfig config = taskWrapper.getConfig();
        if (null == config || StringUtils.isEmpty(config.getField()) || null == config.getFieldTime() || config.getFieldTime() <= 0) {
            return false;
        }
        return hasLimit(Duration.ofSeconds(config.getFieldTime()), getWaitName(config, taskWrapper.getParamWrapper()), redis);
    }


    /**
     * 查看是否有阻挡
     * @param waitTime
     * @param waitField
     * @param redis
     * @return
     */
    private static boolean hasLimit(Duration waitTime, String waitField, StringRedisTemplate redis) {
        return !redis.opsForValue().setIfAbsent(cacheInteral(waitField), "", waitTime);
    }

    /**
     * 计算timeStamp
     * @param waitField 等待字段
     * @param waitTime  等待时间，单位：秒
     * @return
     * 并发安全问题：
     *  1.现在支付的userName决定了打款延时
     *  2.而userName理论上不会有并发
     *  3.最差情况，出现了并发，就会打款失败，不会造成危害
     */
    private static long calc(String waitField, Duration waitTime, StringRedisTemplate redis) {
        long currentTime = System.currentTimeMillis();
        String timeLimitKey = timeStampKey(waitField);
        Long expire = redis.getExpire(timeLimitKey); // 注意：单位：秒
        if (-1L == expire) {
            throw new IllegalStateException("存在不过期的WaitField" + timeLimitKey);
        }
        expire = -2L == expire ? 0L : expire * 1000;
        redis.opsForValue().set(timeLimitKey, "", waitTime.plusMillis(expire));
        return expire + currentTime;
    }

    /**
     * 根据反射获取Field的值
     * @param config
     * @param paramWrapper
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static String getWaitName(AsyncRequestConfig config, ParamWrapper paramWrapper) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
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
    private static String toUpperFirstChar(String string) {
        char[] chars = string.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] -= 32;
            return String.valueOf(chars);
        }
        return string;
    }

    // CACHE：创建限制时间key
    private static String timeStampKey(String waitField) {
        return new StringBuilder(50)
                .append(CACHE_TIME_LIMIT)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(waitField)
                .toString();
    }

    // CACHE：创建限制时间key
    private static String cacheInteral(String waitField) {
        return new StringBuilder(50)
                .append(CACHE_TASK_INTERAL)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(waitField)
                .toString();
    }
}
