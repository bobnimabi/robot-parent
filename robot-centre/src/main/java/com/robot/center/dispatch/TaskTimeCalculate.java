package com.robot.center.dispatch;

import com.robot.center.constant.RobotConsts;
import com.robot.center.execute.TaskWrapper;
import com.robot.center.tenant.TContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by mrt on 10/31/2019 6:31 PM
 */
@Service
@Slf4j
public class TaskTimeCalculate {
    private static final String CACHE_TIME_LIMIT = RobotConsts.ROBOT_PROJECT_PERFIX + "TASK:TIME_CALC:";
    @Autowired
    private StringRedisTemplate redis;

    // 理论上：同一个用户不存在并发,所以这里未使用锁
    public long calcTaskTimeStamp(TaskWrapper taskWrapper) {
        long currentTime = System.currentTimeMillis();
        if (null == taskWrapper.getWaitTime()) {
            return currentTime;
        }
        Assert.hasText(taskWrapper.getWaitField(), "WaitField为空");
        String timeLimitKey = createCacheTimeLimitKey(taskWrapper.getWaitField());
        Long expire = redis.getExpire(timeLimitKey); // 注意：单位：秒
        if (-1L == expire) {
            throw new IllegalStateException("存在不过期的WaitField" + timeLimitKey);
        }
        // 重置过期时间
        expire = -2L == expire ? 0L : expire * 1000;
        redis.opsForValue().set(timeLimitKey, "", taskWrapper.getWaitTime().plusMillis(expire));
        return expire + currentTime;
    }

    // CACHE：创建限制时间key
    private static String createCacheTimeLimitKey(String waitField) {
        return new StringBuilder(45)
                .append(CACHE_TIME_LIMIT)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(waitField)
                .toString();
    }
}
