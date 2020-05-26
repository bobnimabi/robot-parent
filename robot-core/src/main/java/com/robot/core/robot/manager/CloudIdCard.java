package com.robot.core.robot.manager;

import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @Author mrt
 * @Date 2020/5/25 19:40
 * @Version 2.0
 */
@Service
public class CloudIdCard implements ICloudIdCard{

    @Autowired
    private StringRedisTemplate redis;

    /**
     * idCard过期时间3天
     */
    public static final int EXPIRE_DAYS = 3;

    @Override
    public String getIdCard(long robotId) {
        String key = this.idCardKey(robotId);
        String idCard = redis.opsForValue().get(key);
        if (!StringUtils.isEmpty(idCard)) {
            this.expireFlush(robotId);
        }
        return idCard;
    }

    @Override
    public void setIdCard(long robotId,String newIdCard) {
        Assert.notNull(robotId, "不能为null");
        Assert.hasText(newIdCard, "设置新IdCard：newIdCard为空");
        redis.opsForValue().set(this.idCardKey(robotId), newIdCard, Duration.ofDays(EXPIRE_DAYS));
    }

    @Override
    public void delIdCard(long robotId) {
        redis.delete(this.idCardKey(robotId));
    }

    private void expireFlush(long robotId) {
        redis.expire(this.idCardKey(robotId), EXPIRE_DAYS, TimeUnit.DAYS);
    }

    private String idCardKey(long robotId) {
        return new StringBuilder(30)
                .append(RedisConsts.ID_CARD)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(robotId)
                .toString();
    }
}
