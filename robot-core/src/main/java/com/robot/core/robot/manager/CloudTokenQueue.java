package com.robot.core.robot.manager;

import com.alibaba.fastjson.JSON;
import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author mrt
 * @Date 2020/5/25 18:49
 * @Version 2.0
 * 由于redis都开启了aof，故缓存令牌丢失的可能：
 *  1.令牌获取后，正在执行，突然项目挂了，导致令牌未归还
 */
@Slf4j
@Service
public class CloudTokenQueue implements ICloudTokenQueue {
    /**
     * Redis：TOKEN队列标志
     */
    public static final String TOKEN_QUEUE = RedisConsts.PROJECT + "TOKEN_QUEUE:";
    /**
     * TokenQueue过期时间3天
     */
    public static final int EXPIRE_DAYS = 3;

    @Autowired
    private ICloudIdCard cloudIdCard;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Token> redis;

    @Override
    public Token popToken() {
        Token token = redis.opsForList().rightPop(tokenQueueKey());
        if (null != token) {
            log.info("令牌出队：RobotID:{}", token.getRobotId());
            return isTokenValid(token) ? token : null;
        }
        return null;
    }

    @Override
    public boolean pushToken(Token token) {
        log.info("令牌归还：RobotID:{}", token.getRobotId());
        return isTokenValid(token) ? redis.opsForList().leftPush(tokenQueueKey(), token) > 0 : false;
    }

    private boolean isTokenValid(Token token) {
        String idCard = cloudIdCard.getIdCard(token.getRobotId());
        boolean isValid = null != token
                && !StringUtils.isEmpty(token.getIdCard())
                && null != token.getRobotId()
                && !StringUtils.isEmpty(idCard)
                && token.getIdCard().equalsIgnoreCase(idCard);

        if (!isValid) {
            log.info("无效令牌->销毁，TOKEN:{},cloudIdCard:{}", JSON.toJSONString(token), idCard);
        } else {
            this.expireFlush();
        }
        return isValid;
    }

    private void expireFlush() {
        String key = this.tokenQueueKey();
        Boolean isFailure = redis.hasKey(key) && !redis.expire(key, EXPIRE_DAYS, TimeUnit.DAYS);
        if (isFailure) {
            log.error("令牌队列:刷新过期时间失败：key：{}", key);
        }
    }

    private String tokenQueueKey() {
        return new StringBuilder(35)
                .append(TOKEN_QUEUE)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction())
                .toString();
    }
}
