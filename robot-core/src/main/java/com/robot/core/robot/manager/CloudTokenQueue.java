package com.robot.core.robot.manager;

import com.robot.core.common.TContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.concurrent.TimeUnit;
import static com.robot.core.common.RedisConsts.TOKEN_QUEUE;

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
     * TokenQueue过期时间3天
     */
    public static final int EXPIRE_DAYS = 3;

    @Autowired
    private ICloudIdCard cloudIdCard;

    @Autowired
    private RedisTemplate<String, Token> redis;

    @Override
    public Token popToken() {
        Token token = redis.opsForList().rightPop(tokenQueueKey());
        boolean isTokenValid = isTokenValid(token);
        return isTokenValid ? token : null;
    }

    @Override
    public boolean pushToken(Token token) {
        boolean isTokenValid = isTokenValid(token);
        if (isTokenValid) {
            Long aLong = redis.opsForList().leftPush(tokenQueueKey(), token);
            return aLong > 0;
        }
        return false;
    }

    private boolean isTokenValid(Token token) {
        String idCard = cloudIdCard.getIdCard(token.getRobotId());
        boolean isValid = null != token
                && !StringUtils.isEmpty(token.getIdCard())
                && null != token.getRobotId()
                && !StringUtils.isEmpty(idCard)
                && token.getIdCard().equalsIgnoreCase(idCard);

        if (!isValid) {
            log.info("CloudTokenQueue：无效TOKEN， 即将销毁...");
        } else {
            this.expireFlush();
        }
        return isValid;
    }

    private void expireFlush() {
        redis.expire(this.tokenQueueKey(), EXPIRE_DAYS, TimeUnit.DAYS);
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
