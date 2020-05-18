package com.robot.core.httpclient.chain;

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
public class ConnectionPoolChain extends HttpClientFilter<HttpClientBuilder> {
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
     * 无效连接校验时间，单位：秒
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
    public boolean dofilter(HttpClientBuilder httpClientBuilder) throws Exception {
        ConnectionPoolConfig poolConfig = poolConfigService.getPoolConfig();
        Optional<Integer> maxTotal = Optional.of(poolConfig.getMaxTotal());
        Optional<Integer> maxPerRoute = Optional.of(poolConfig.getMaxPerRoute());
        Optional<Integer> validateAfterInactivity = Optional.of(poolConfig.getValidateAfterInactivity());
        Optional<Integer> sleepTime = Optional.of(poolConfig.getSleepTime());
        Optional<Integer> maxIdleTime = Optional.of(poolConfig.getMaxIdleTime());
        // 过期和空闲连接策略
        httpClientBuilder.evictExpiredConnections();
        httpClientBuilder.evictIdleConnections(maxIdleTime.orElse(MAX_IDLE_TIME), TimeUnit.SECONDS);
        httpClientBuilder.setConnectionManager(createPoolingHttpClientConnectionManager(
                maxTotal.orElse(MAX_TOTAL),
                maxPerRoute.orElse(MAX_PER_ROUTE),
                validateAfterInactivity.orElse(VALIDATE_AFTER_INACTIVITY)

        ));
        log.info("配置：连接池：{}", 1);
        return true;
    }

    @Override
    public int order() {
        return 5;
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
