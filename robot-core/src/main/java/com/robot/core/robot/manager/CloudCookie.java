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
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Date;
import java.util.Iterator;
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
     * Redis：机器人COOKIE标志
     * 7
     */
    public static final String COOKIE = RedisConsts.PROJECT + "COOKIE:";
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
            this.clearExpireCookie(robotWrapper);
            expireFlush(key);
            log.info("CloudCookie:获取到机器人：robotId：{}", robotId);
        }
        return robotWrapper;
    }

    @Override
    public boolean putCookie(RobotWrapper robotWrapper) {
        if (!isRobotWrapperValid(robotWrapper)) {
            log.info("CloudCookie:put的robotWrapper无效，请检查：{}", JSON.toJSONString(robotWrapper));
            return false;
        }
        redis.opsForValue().set(this.cookieKey(robotWrapper.getId()), robotWrapper, Duration.ofDays(EXPIRE_DAYS));
        return true;
    }

    private boolean isRobotWrapperValid(RobotWrapper robotWrapper) {
        return null != robotWrapper && null != robotWrapper.getCookieStore();
    }

    private void expireFlush(String key) {
        Boolean isFailure = !redis.expire(key, EXPIRE_DAYS, TimeUnit.DAYS) && redis.hasKey(key);
        if (isFailure) {
            log.error("CloudCookie:刷新过期时间失败：key：{}", key);
        }
    }

    private void clearExpireCookie(RobotWrapper robotWrapper) {
        List<Cookie> cookies = robotWrapper.getCookieStore().getCookies();
        Date date = new Date();
        Iterator<Cookie> iterator = cookies.iterator();
        while (iterator.hasNext()) {
            Cookie cookie = iterator.next();
            boolean expired = cookie.isExpired(date);
            if (expired) {
                log.info("CloudCookie:删除过期Cookie：{}", JSON.toJSONString(cookie));
            }
        }
    }

    private String cookieKey(long robotId) {
        return new StringBuilder(30)
                .append(COOKIE)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(robotId)
                .toString();
    }
}
