package com.robot.center.httpclient.strategy;

import com.robot.center.httpclient.HttpClientConfig;
import com.robot.center.httpclient.HttpClientFilter;
import com.robot.center.httpclient.HttpClientInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/3/13 16:21
 */
@Slf4j
@Service
public class ConnectionPoolStrategy extends HttpClientFilter<HttpClientInvocation> {
    // 连接池最大连接数
    private static final int maxTotal = 100;
    // 默认路由并发数
    private static final int defaultMaxPerRoute = 10;
    // 无效连接校验时间，单位：秒
    private static final int validateAfterInactivity = 1;
    // 空闲和过期连接检查时间间隔，单位：秒
    private static final int sleepTime = 30;
    // 最大空闲时间，单位：秒
    private static final int maxIdleTime = 30;

    @Override
    protected boolean dofilter(HttpClientInvocation invocation) throws Exception {
        HttpClientBuilder httpClientBuilder = invocation.getHttpClientBuilder();
        httpClientBuilder.setConnectionManager(createPoolingHttpClientConnectionManager(invocation.getConfig()));
        log.info("配置：连接池：{}", 1);
        return true;
    }

    /**
     * 创建池
     * @param config
     * @return
     */
    private PoolingHttpClientConnectionManager createPoolingHttpClientConnectionManager(HttpClientConfig config) {
        PoolingHttpClientConnectionManager poolmanager = new PoolingHttpClientConnectionManager();
        poolmanager.setMaxTotal(config.getMaxTotal());
        poolmanager.setDefaultMaxPerRoute(config.getDefaultMaxPerRoute());
        poolmanager.setValidateAfterInactivity(config.getValidateAfterInactivity() * 1000);
        return poolmanager;
    }
}
