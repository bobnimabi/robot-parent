package com.robot.core.task.dispatcher;

import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.time.Duration;

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
     * 计算任务执行时刻
     *
     * @param waitTime  执行等待时间,单位：秒,null表示不等待
     * @param waitField 等待字段
     * @return 理论上：同一个用户不存在并发,所以这里未使用锁
     */
    public static long calc(Duration waitTime, String waitField, StringRedisTemplate redis) {
        long currentTime = System.currentTimeMillis();
        if (null == waitTime || Duration.ZERO.equals(waitTime)) {
            return currentTime;
        }
        Assert.hasText(waitField, "WaitField为空");
        String timeLimitKey = timeStampKey(waitField);
        Long expire = redis.getExpire(timeLimitKey); // 注意：单位：秒
        if (-1L == expire) {
            throw new IllegalStateException("存在不过期的WaitField" + timeLimitKey);
        }
        // 重置过期时间
        expire = -2L == expire ? 0L : expire * 1000;
        redis.opsForValue().set(timeLimitKey, "", waitTime.plusMillis(expire));
        return expire + currentTime;
    }

    public static boolean hasLimit(Duration waitTime, String waitField, StringRedisTemplate redis) {
        if (null == waitTime || Duration.ZERO.equals(waitTime)) {
            return false;
        }
        Boolean isSet = redis.opsForValue().setIfAbsent(cacheInteral(waitField), "", waitTime);
        return !isSet;
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
