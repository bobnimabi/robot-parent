package com.robot.core.httpclient.factory;

import com.robot.core.chain.Invoker;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.stereotype.Service;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Created by mrt on 2020/3/13 17:09
 * SSL策略：允许所有自签名证书
 */
@Slf4j
@Service
public class SslChain extends BuilderFilter<Object,HttpClientBuilder> {

    /**
     * 这样设置不生效，以后找原因
     * // httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);//设置不生效，以后解决
     * // httpClientBuilder.setSSLContext(createSSLContext());//设置不生效，以后解决
     * @param params
     * @param result
     * @param invoker
     * @throws Exception
     */
    @Override
    public void dofilter(Object params, HttpClientBuilder result, Invoker<Object, HttpClientBuilder> invoker) throws Exception {
        result.setConnectionManager(SslHttpClientBuild());
        log.info("配置：SSL策略：放行所有自制证书，加载完成");

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 1;
    }


    private SSLContext createSSLContext() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return SSLContexts.custom().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
    }

    /**
     * 跳过https的证书检验，允许自制证书
     * @return
     */
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
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            //don't check
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            //don't check
        }
    }
}
