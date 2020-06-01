package com.robot.core.robot.manager;

import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @Author mrt
 * @Date 2020/5/25 19:40
 * @Version 2.0
 */
@Slf4j
@Service
public class CloudIdCard implements ICloudIdCard{
    /**
     * Redis：机器人ID_CARD标志
     */
    public static final String ID_CARD = RedisConsts.PROJECT + "ID_CARD:";

    @Autowired
    private StringRedisTemplate redis;

    /**
     * idCard过期时间3天
     */
    public static final int EXPIRE_DAYS = 30;

    @Override
    public String getIdCard(long robotId) {
        String key = this.idCardKey(robotId);
        String idCard = redis.opsForValue().get(key);
        if (!StringUtils.isEmpty(idCard)) {
            this.expireFlush(key);
        }
        return idCard;
    }

    @Override
    public boolean setIdCard(long robotId,String newIdCard) {
        if (StringUtils.isEmpty(newIdCard)) {
            log.error("CloudIdCard：newIdCard为空");
            return false;
        }
        redis.opsForValue().set(this.idCardKey(robotId), newIdCard, Duration.ofDays(EXPIRE_DAYS));
        return true;
    }

    @Override
    public boolean delIdCard(long robotId) {
        String key = this.idCardKey(robotId);
        boolean isFailure = !redis.delete(key) && redis.hasKey(key);
        if (isFailure) {
            log.error("CloudIdCard:删除ID_CARD失败：robotId:{}", robotId);
        }
        return !isFailure;
    }

    private void expireFlush(String key) {
        Boolean isFailure = !redis.expire(key, EXPIRE_DAYS, TimeUnit.DAYS) && redis.hasKey(key);
        if (isFailure) {
            log.error("CloudIdCard:刷新过期时间失败：key：{}", key);
        }
    }

    private String idCardKey(long robotId) {
        return new StringBuilder(30)
                .append(ID_CARD)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(robotId)
                .toString();
    }
}
