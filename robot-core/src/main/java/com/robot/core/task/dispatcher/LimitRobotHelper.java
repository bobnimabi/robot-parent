package com.robot.core.task.dispatcher;

import com.robot.code.entity.AsyncRequestConfig;
import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Created by mrt on 11/1/2019 12:22 PM
 */
@Slf4j
@Service
public class LimitRobotHelper {
    /**
     * redis：机器人阻挡前缀
     */
    private static final String CACHE_ROBOT_ME_LIMIT = RedisConsts.PROJECT + "ROBOT:LIMIT:";

    /**
     * 机器人出队时判断是否有阻挡
     * @param taskWrapper
     * @param robotId
     * @param redis
     * @return
     */
    public static boolean hasLimit(TaskWrapper taskWrapper, long robotId, StringRedisTemplate redis) {
        AsyncRequestConfig config = taskWrapper.getConfig();
        if (null == config || null == config.getRobotTimeLimit() || config.getRobotTimeLimit() <= 0 || null == taskWrapper.getPathEnum()) {
            return false;
        }
        return hasLimit(Duration.ofSeconds(config.getRobotTimeLimit()), taskWrapper.getPathEnum().getPathCode(), robotId, redis);
    }

    /**
     * 判断是否有对机器人的阻挡
     * @param waitTime
     * @param pathCode
     * @param robotId
     * @param redis
     * @return
     */
    private static boolean hasLimit(Duration waitTime, String pathCode, long robotId, StringRedisTemplate redis) {
        return !redis.opsForValue().setIfAbsent(robotLimitKey(pathCode, robotId), "", waitTime);
    }

    // CACHE：创建机器人执行Field限制时间key
    private static String robotLimitKey(String pathCode, long robotId) {
        return new StringBuilder(45)
                .append(CACHE_ROBOT_ME_LIMIT)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(pathCode).append(":")
                .append(robotId)
                .toString();
    }
}
