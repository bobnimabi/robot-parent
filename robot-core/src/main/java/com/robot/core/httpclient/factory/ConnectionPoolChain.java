package com.robot.core.httpclient.factory;

import com.robot.code.entity.ConnectionPoolConfig;
import com.robot.code.service.IConnectionPoolConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 2020/3/13 16:21
 */
@Slf4j
@Service
public class ConnectionPoolChain extends BuilderFilter<HttpClientBuilder> {
    @Autowired
    private IConnectionPoolConfigService poolConfigService;

    /**
     * 连接池最大连接数
      */
    private static final int MAX_TOTAL = 5;

    /**
     * 默认路由并发数
     */
    private static final int MAX_PER_ROUTE = 1;

    /**
     * 无活动校验时间间隔，单位：秒
     */
    private static final int VALIDATE_AFTER_INACTIVITY = 1;

    /**
     * 空闲和过期连接检查时间间隔，单位：秒
     * 暂时没用上
     */
    private static final int SLEEP_TIME = 30;

    /**
     * 最大空闲时间，单位：秒
     */
    private static final int MAX_IDLE_TIME = 30;


    @Override
    public boolean dofilter(HttpClientBuilder params) throws Exception {
        ConnectionPoolConfig poolConfig = poolConfigService.getPoolConfig();
        Integer maxTotal = Optional.of(poolConfig.getMaxTotal()).orElse(MAX_TOTAL);
        Integer maxPerRoute = Optional.of(poolConfig.getMaxPerRoute()).orElse(MAX_PER_ROUTE);
        Integer validateAfterInactivity = Optional.of(poolConfig.getValidateAfterInactivity()).orElse(VALIDATE_AFTER_INACTIVITY);
        Integer sleepTime = Optional.of(poolConfig.getSleepTime()).orElse(SLEEP_TIME);
        Integer maxIdleTime = Optional.of(poolConfig.getMaxIdleTime()).orElse(MAX_IDLE_TIME);
        // 过期和空闲连接策略
        params.evictExpiredConnections();
        params.evictIdleConnections(maxIdleTime, TimeUnit.SECONDS);
        params.setConnectionManager(createPoolingHttpClientConnectionManager(
                maxTotal,
                maxPerRoute,
                validateAfterInactivity

        ));
        log.info("配置：连接池策略：MAX_TOTAL：{}，MAX_PER_ROUTE：{}，VALIDATE_AFTER_INACTIVITY：{}，MAX_IDLE_TIME：{}，",
                maxTotal, maxPerRoute, validateAfterInactivity, maxIdleTime);
        return true;
    }

    @Override
    public int order() {
        return 9;
    }

    /**
     * 创建连接池
     * @param maxTotal
     * @param defaultMaxPerRoute
     * @param ValidateAfterInactivity
     * @return
     */
    private PoolingHttpClientConnectionManager createPoolingHttpClientConnectionManager(
            int maxTotal,int defaultMaxPerRoute,int ValidateAfterInactivity
    ) {
        PoolingHttpClientConnectionManager poolmanager = new PoolingHttpClientConnectionManager();
        poolmanager.setMaxTotal(maxTotal);
        poolmanager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        poolmanager.setValidateAfterInactivity(ValidateAfterInactivity * 1000);
        return poolmanager;
    }
}
