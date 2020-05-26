package com.robot.core.robot.manager;

import com.alibaba.fastjson.JSON;
import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author mrt
 * @Date 2020/5/25 20:34
 * @Version 2.0
 */
@Slf4j
@Service
public class CloudCookie implements ICloudCookie {
    /**
     * Cookie过期时间30天
     */
    public static final int EXPIRE_DAYS = 30;

    @Autowired
    private RedisTemplate<String,RobotWrapper> redis;

    @Autowired
    private ICloudIdCard cloudIdCard;

    @Override
    public RobotWrapper getCookie(long robotId) {
        String key = cookieKey(robotId);
        RobotWrapper robotWrapper = redis.opsForValue().get(key);
        if (null != robotWrapper) {
            if (isCookieExpire(robotWrapper) || isIdCardExpire(robotWrapper)) {
                this.delCookie(robotId);
                return null;
            }
            expireFlush(key);
            log.info("CloudCookie:获取到机器人：robotId：{}", robotId);
        }
        return robotWrapper;
    }

    @Override
    public boolean putCookie(RobotWrapper robotWrapper) {
        if (isCookieExpire(robotWrapper) || isIdCardExpire(robotWrapper)) {
            log.error("CloudCookie:存入机器人不合法,将被销毁，RobotWrapper:{}", JSON.toJSONString(robotWrapper));
            this.delCookie(robotWrapper.getId());
            return false;
        }
        redis.opsForValue().set(this.cookieKey(robotWrapper.getId()), robotWrapper, Duration.ofDays(EXPIRE_DAYS));
        return true;
    }

    private void delCookie(long robotId) {
        String key = cookieKey(robotId);
        boolean isFailure = !redis.delete(key) && redis.hasKey(key);
        if (isFailure) {
            log.error("CloudCookie:删除Cookie失败：robotId：{}", robotId);
        }
    }

    private void expireFlush(String key) {
        Boolean isFailure = !redis.expire(key, EXPIRE_DAYS, TimeUnit.DAYS) && redis.hasKey(key);
        if (isFailure) {
            log.error("CloudCookie:刷新过期时间失败：key：{}", key);
        }
    }

    private boolean isCookieExpire(RobotWrapper robotWrapper) {
        CookieStore cookieStore = robotWrapper.getCookieStore();
        if (null == cookieStore) {
            log.error("CloudCookie:CookieStore为null，将销毁，CookieStore：{}", JSON.toJSONString(robotWrapper));
            return true;
        }
        List<Cookie> cookies = robotWrapper.getCookieStore().getCookies();
        if (CollectionUtils.isEmpty(cookies)) {
            log.error("CloudCookie:cookies为空集合，将销毁，CookieStore：{}", JSON.toJSONString(robotWrapper));
            return true;
        }
        Date date = new Date();
        for (Cookie cookie : cookies) {
            boolean expired = cookie.isExpired(date);
            if (expired) {
                log.error("CloudCookie:有cookie已过期，将销毁，CookieStore：{}", JSON.toJSONString(robotWrapper));
                return true;
            }
        }
        return false;
    }

    private boolean isIdCardExpire(RobotWrapper robotWrapper) {
        String idCard = cloudIdCard.getIdCard(robotWrapper.getId());
        String rIdCard = robotWrapper.getIdCard();
        boolean isIdCardExpire = StringUtils.isEmpty(rIdCard) || StringUtils.isEmpty(idCard) || !rIdCard.equalsIgnoreCase(idCard);
        if (isIdCardExpire) {
            log.error("CloudCookie:IDCard不合法，将销毁，IDCard：{}，机器人的IDCard：{}", idCard, rIdCard);
        }
        return isIdCardExpire;
    }

    private String cookieKey(long robotId) {
        return new StringBuilder(30)
                .append(RedisConsts.COOKIE)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(robotId)
                .toString();
    }
}
