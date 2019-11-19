package com.robot.center.dispatch;

import com.robot.center.constant.RobotConsts;
import com.robot.center.execute.TaskWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 10/31/2019 6:31 PM
 */
@Service
@Slf4j
public class TaskTimeCalculate {
    private static final String CACHE_TIME_LIMIT = RobotConsts.ROBOT_PROJECT_PERFIX + "TASK:TIME_LIMIT:";
    @Autowired
    private StringRedisTemplate redis;

    // 理论上：同一个用户不存在并发,所以这里未使用锁
    public long calcTaskTimeStamp(TaskWrapper taskWrapper) {
        Date date = new Date();
        long currentTime = date.getTime();
        if (null == taskWrapper.getWaitTime()) {
            return currentTime;
        }
        Assert.hasText(taskWrapper.getWaitField(), "WaitField为空");
        String timeLimitKey = createCacheTimeLimitKey(taskWrapper.getWaitField());
        Long expire = redis.getExpire(timeLimitKey); // 注意：单位：秒
        if (-2 == expire) {
            redis.opsForValue().set(timeLimitKey, currentTime + "", taskWrapper.getWaitTime());
            return currentTime;
        } else if (expire >= 0) {
            Long increment = redis.opsForValue().increment(timeLimitKey, taskWrapper.getWaitTime().toMillis());
            redis.expire(timeLimitKey, taskWrapper.getWaitTime().plusSeconds(expire).toMillis(), TimeUnit.MILLISECONDS);
            return increment;
        } else {
            throw new IllegalStateException("存在不过期的WaitField" + timeLimitKey);
        }
    }

    // CACHE：创建限制时间key
    private static String createCacheTimeLimitKey(String waitField) {
        return new StringBuilder(45)
                .append(CACHE_TIME_LIMIT)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction()).append(":")
                .append(waitField)
                .toString();
    }
}
