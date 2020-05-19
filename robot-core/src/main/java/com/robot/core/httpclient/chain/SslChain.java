package com.robot.core.httpclient.chain;

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
public class SslChain extends BuilderFilter<HttpClientBuilder> {

    @Override
    public boolean dofilter(HttpClientBuilder httpClientBuilder) throws Exception {
//        httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);//设置不生效，以后解决
//        httpClientBuilder.setSSLContext(createSSLContext());//设置不生效，以后解决
        httpClientBuilder.setConnectionManager(SslHttpClientBuild());
        log.info("配置：SSL验证证书策略：放行所有自签名证书");
        return true;
    }

    @Override
    public int order() {
        return 1;
    }


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
}
