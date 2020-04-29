package com.robot.center.httpclient.chain;

import com.robot.center.httpclient.HttpClientConfig;
import com.robot.center.httpclient.HttpClientFilter;
import com.robot.center.httpclient.HttpClientInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 2020/3/13 16:21
 */
@Slf4j
@Service
public class ConnectionPoolChain extends HttpClientFilter<HttpClientInvocation> {
    // 连接池最大连接数
    private static final int MAX_TOTAL = 100;
    // 默认路由并发数
    private static final int MAX_PER_ROUTE = 10;
    // 无效连接校验时间，单位：秒
    private static final int VALIDATE_AFTER_INACTIVITY = 1;
    // 空闲和过期连接检查时间间隔，单位：秒
    private static final int SLEEP_TIME = 30;
    // 最大空闲时间，单位：秒
    private static final int MAX_IDLE_TIME = 30;

    @Override
    public boolean dofilter(HttpClientInvocation invocation) throws Exception {
        HttpClientBuilder httpClientBuilder = invocation.getHttpClientBuilder();
        // 过期和空闲连接策略
        httpClientBuilder.evictExpiredConnections();
        httpClientBuilder.evictIdleConnections(MAX_IDLE_TIME, TimeUnit.SECONDS);
        httpClientBuilder.setConnectionManager(createPoolingHttpClientConnectionManager(invocation.getConfig()));
        log.info("配置：连接池：{}", 1);
        return true;
    }

    @Override
    public int order() {
        return 0;
    }

    /**
     * 创建池
     * @param config
     * @return
     */
    private PoolingHttpClientConnectionManager createPoolingHttpClientConnectionManager(HttpClientConfig config) {
        PoolingHttpClientConnectionManager poolmanager = new PoolingHttpClientConnectionManager();
        poolmanager.setMaxTotal(config.getMaxTotal()== null?MAX_TOTAL:config.get);
        poolmanager.setDefaultMaxPerRoute(config.getDefaultMaxPerRoute());
        poolmanager.setValidateAfterInactivity(config.getValidateAfterInactivity() * 1000);
        return poolmanager;
    }
}
