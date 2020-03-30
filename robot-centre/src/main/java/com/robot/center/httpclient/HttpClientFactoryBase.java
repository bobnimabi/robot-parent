package com.robot.center.httpclient;

import com.robot.center.pool.RobotManager;
import com.netflix.http4.NFHttpMethodRetryHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 10/18/2019 3:11 PM
 */
@Data
@Slf4j
public abstract class HttpClientFactoryBase {

    @Autowired
    private HttpClientConfig customHttpClientConfig;

    @Autowired
    private RobotManager robotManager;

    /**
     * 创建httpclient
     * @param requestInterceptors
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    protected CloseableHttpClient createCustomHttpClient(long robotId, List<HttpRequestInterceptor> requestInterceptors) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        HttpClientConfig config = customHttpClientConfig.getCustomHttpClientConfig(robotId);

        // 设置连接池
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setConnectionManager(createPoolingHttpClientConnectionManager(config));

        // 设置全局头
        httpClientBuilder.setDefaultHeaders(config.getCommonHeaders().getHeaders());

        // 设置keep-alive策略
        httpClientBuilder.setKeepAliveStrategy();

        // 设置请求策略
        httpClientBuilder.setDefaultRequestConfig(createRequestConfig(config));
        httpClientBuilder.setRedirectStrategy(createRedirectStrategy());

        // 过期和空闲连接策略
        httpClientBuilder.evictExpiredConnections();
        httpClientBuilder.evictIdleConnections(config.getMaxIdleTime(), TimeUnit.SECONDS);

        // SSL 信任所有的证书,防止自制证书无法使用的问题
//        httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);//设置不生效，以后解决
//        httpClientBuilder.setSSLContext(createSSLContext());//设置不生效，以后解决
        httpClientBuilder.setConnectionManager(SslHttpClientBuild());

        // 重试开启，默认3次
        httpClientBuilder.setRetryHandler(createHttpRequestRetryHandler("未定义httpclientName", config.getRetryCount(), true, 1000));

        // 请求拦截器
        if (!CollectionUtils.isEmpty(requestInterceptors)) {
            requestInterceptors.forEach(o->httpClientBuilder.addInterceptorFirst(o));
        }
        return httpClientBuilder.build();
        RequestConfig
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
