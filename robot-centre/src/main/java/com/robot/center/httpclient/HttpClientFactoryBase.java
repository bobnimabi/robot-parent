package com.robot.center.httpclient;

import com.robot.center.pool.RobotManager;
import com.netflix.http4.NFHttpMethodRetryHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.Args;
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
    private CustomHttpClientConfig customHttpClientConfig;
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

        CustomHttpClientConfig config = customHttpClientConfig.getCustomHttpClientConfig(robotId);

        // 设置连接池
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setConnectionManager(createPoolingHttpClientConnectionManager(config));

        // 设置全局头
        httpClientBuilder.setDefaultHeaders(config.getCommonHeaders().getHeaders());

        // 设置keep-alive策略
        httpClientBuilder.setKeepAliveStrategy(createConnectionKeepAliveStrategy(config));

        // 设置请求策略
        httpClientBuilder.setDefaultRequestConfig(createRequestConfig(config));

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

        // 设置代理
        if (!StringUtils.isEmpty(config.getProxyIp()) && !StringUtils.isEmpty(config.getProxyPort())) {
            httpClientBuilder.setProxy(new HttpHost(config.getProxyIp(), Integer.parseInt(config.getProxyPort())));
        }
        return httpClientBuilder.build();
    }

    /**
     * 创建池
     * @param config
     * @return
     */
    private PoolingHttpClientConnectionManager createPoolingHttpClientConnectionManager(CustomHttpClientConfig config) {
        PoolingHttpClientConnectionManager poolmanager = new PoolingHttpClientConnectionManager();
        poolmanager.setMaxTotal(config.getMaxTotal());
        poolmanager.setDefaultMaxPerRoute(config.getDefaultMaxPerRoute());
        poolmanager.setValidateAfterInactivity(config.getValidateAfterInactivity() * 1000);
        return poolmanager;
    }

    /**
     * keep-alive策略
     * @param config
     * @return
     */
    private ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy(CustomHttpClientConfig config) {
        return new ConnectionKeepAliveStrategy() {

            @Override
            public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                Args.notNull(httpResponse, "HTTP response");
                BasicHeaderElementIterator it = new BasicHeaderElementIterator(httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while(true) {
                    String param;
                    String value;
                    do {
                        do {
                            if (!it.hasNext()) {
                                return config.getKeepAliveTimeout() * 1000L;
                            }
                            HeaderElement he = it.nextElement();
                            param = he.getName();
                            value = he.getValue();
                        } while(value == null);
                    } while(!param.equalsIgnoreCase("timeout"));

                    try {
                        return Long.parseLong(value) * 1000L;
                    } catch (NumberFormatException e) {
                    }
                }
            }
        };
    }

    /**
     * SSL 信任所有的证书：防止自制证书
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private SSLContext createSSLContext() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return SSLContexts.custom().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
    }

    public static PoolingHttpClientConnectionManager SslHttpClientBuild() {
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.INSTANCE)
                        .register("https", trustAllHttpsCertificates()).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        return connectionManager;
    }

    private static SSLConnectionSocketFactory trustAllHttpsCertificates() {
        SSLConnectionSocketFactory socketFactory = null;
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = TrustCheck.INSTANCE;
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, null);
            socketFactory = new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return socketFactory;
    }
    private static class TrustCheck implements TrustManager, X509TrustManager {
        public static final TrustCheck INSTANCE = new TrustCheck();
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            //don't check
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            //don't check
        }
    }


    /**
     * 请求配置
     * @param config
     * @return
     */
    private RequestConfig createRequestConfig(CustomHttpClientConfig config) {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(config.getConnectionRequestTimeout() * 1000) // 从连接池中取连接的超时时间
                .setConnectTimeout(config.getConnectTimeout() * 1000) // 连接超时时间
                .setSocketTimeout(config.getSocketTimeout() * 1000) // 请求超时时间
                .setRelativeRedirectsAllowed(false)
                .setRedirectsEnabled(false)
                .build();
    }

    /**
     * 重试机制
     * @param httpClientName
     * @param retryCount
     * @param requestSentRetryEnabled
     * @param sleepTimeFactorMs
     * @return
     */
    private HttpRequestRetryHandler createHttpRequestRetryHandler(String httpClientName, int retryCount, boolean requestSentRetryEnabled, int sleepTimeFactorMs) {
        return new CustomHttpRequestRetryHandler(httpClientName, retryCount, requestSentRetryEnabled, sleepTimeFactorMs);
    }

    private static final class CustomHttpRequestRetryHandler extends NFHttpMethodRetryHandler {

        // 初始化可重试的方法
        private final Map<String, Boolean> idempotentMethods;

        public CustomHttpRequestRetryHandler(String httpClientName, int retryCount, boolean requestSentRetryEnabled, int sleepTimeFactorMs) {
            super(httpClientName, retryCount, requestSentRetryEnabled, sleepTimeFactorMs);
            this.idempotentMethods = new ConcurrentHashMap();
            this.idempotentMethods.put("GET", Boolean.TRUE);
            this.idempotentMethods.put("HEAD", Boolean.TRUE);
            this.idempotentMethods.put("DELETE", Boolean.TRUE);
            this.idempotentMethods.put("OPTIONS", Boolean.TRUE);
            this.idempotentMethods.put("TRACE", Boolean.TRUE);
        }

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            return super.retryRequest(exception, executionCount, context);
        }

        @Override
        protected boolean handleAsIdempotent(HttpRequest request) {
            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
            Boolean b = (Boolean)this.idempotentMethods.get(method);
            return b != null && b;
        }
    }
}
