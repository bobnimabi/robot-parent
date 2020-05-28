package com.robot.core.task.dispatcher;

import com.robot.code.service.ITenantRobotProxyService;
import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import com.robot.core.function.base.IActionEnum;
import com.robot.core.function.base.IFunction;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 11/1/2019 12:22 PM
 */
@Slf4j
@Service
public class LimitRobotHelper {

    private static final String CACHE_ROBOT_ME_LIMIT = RedisConsts.PROJECT + "ROBOT:LIMIT:";

    @Autowired
    private StringRedisTemplate redis;


    public static boolean hasLimit(Duration waitTime, String actionCode, long robotId, StringRedisTemplate redis) {
        if (null == waitTime || Duration.ZERO.equals(waitTime)) {
            return false;
        }
        Boolean isSet = redis.opsForValue().setIfAbsent(robotLimitKey(actionCode, robotId), "", waitTime);
        return !isSet;
    }

    // CACHE：创建机器人执行Field限制时间key
    private static String robotLimitKey(String actionCode, long robotId) {
        return new StringBuilder(45)
                .append(CACHE_ROBOT_ME_LIMIT)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(actionCode).append(":")
                .append(robotId)
                .toString();
    }
}
