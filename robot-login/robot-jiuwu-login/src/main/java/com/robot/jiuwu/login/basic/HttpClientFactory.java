package com.robot.jiuwu.login.basic;

import com.robot.center.constant.RobotConsts;
import com.robot.center.httpclient.AbstractHttpClientFactory;
import com.robot.jiuwu.login.function.LoginInServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 11/18/2019 12:45 PM
 * 注意：95棋牌的所有请求都需要带上token（token来自于登录响应）
 */
@Slf4j
@Service
public class HttpClientFactory extends AbstractHttpClientFactory {

    @Autowired
    private StringRedisTemplate redis;

    @Override
    protected List<HttpRequestInterceptor> getRequestInterceptor() {
        List<HttpRequestInterceptor> list = new ArrayList<>(1);
        list.add(new TokenInterceptor());
        return list;
    }

    /**
     * Token拦截器
     */
    private class TokenInterceptor implements HttpRequestInterceptor{
        @Override
        public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
            Long robotId = (Long)httpContext.getAttribute(RobotConsts.ROBOT_ID);
            String cacheKeyLoginToken = LoginInServer.createCacheKeyLoginToken(robotId);
            String token = redis.opsForValue().get(cacheKeyLoginToken);
            if (!StringUtils.isEmpty(token)) {
                Boolean expire = redis.expire(cacheKeyLoginToken, 1, TimeUnit.DAYS);
                if (!expire) {
                    log.info("刷新九五棋牌Token失败,RobotId:{}", robotId);
                }
            }
            httpRequest.addHeader("token",token);
        }
    }
}
