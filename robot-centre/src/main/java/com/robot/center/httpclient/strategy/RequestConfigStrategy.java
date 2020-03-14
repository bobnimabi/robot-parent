package com.robot.center.httpclient.strategy;

import com.robot.center.httpclient.HttpClientConfig;
import com.robot.center.httpclient.HttpClientFilter;
import com.robot.center.httpclient.HttpClientInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/3/13 15:22
 */
@Slf4j
@Service
public class RequestConfigStrategy extends HttpClientFilter<HttpClientInvocation> {
    // 是否重定向 1是  0否
    private static final boolean isRedirect = false;
    // 从连接池获取连接超时，单位：秒
    private int connectionRequestTimeout = 5;
    // 建立连接超时，单位：秒
    private int connectTimeout = 10;
    // 响应超时，单位：秒
    private int responseTimeout = 20;


    @Override
    protected boolean dofilter(HttpClientInvocation invocation) throws Exception {
        int keepAliveTimeout = invocation.getConfig().getRe();
        HttpClientBuilder httpClientBuilder = invocation.getHttpClientBuilder();
        log.info("配置：RequestConfig：{}",1);
        return true;
    }

    /**
     * 请求配置
     * @param config
     * @return
     */
    private RequestConfig createRequestConfig(HttpClientConfig config) {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(config.getConnectionRequestTimeout() * 1000) // 从连接池中取连接的超时时间
                .setConnectTimeout(config.getConnectTimeout() * 1000) // 连接超时时间
                .setSocketTimeout(config.getSocketTimeout() * 1000) // 请求超时时间
                .setRelativeRedirectsAllowed(true)
                .setRedirectsEnabled(true)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
    }
}
