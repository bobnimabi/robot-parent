package com.robot.core.httpclient.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/3/13 16:52
 * 重定向策略：允许所有方法重定向，请求是否重定向由请求配置决定
 */
@Slf4j
@Service
public class RedirectChain extends BuilderFilter<HttpClientBuilder> {

    // 可重定向的方法
    private static final String[] CUSTOM_REDIRECT_METHODS = {"GET", "HEAD","POST","DELETE","PUT"};

    @Override
    public boolean dofilter(HttpClientBuilder httpClientBuilder) throws Exception {
        httpClientBuilder.setRedirectStrategy(createRedirectStrategy());
        log.info("配置：重定向策略：方法：{}", CUSTOM_REDIRECT_METHODS);
        return true;
    }

    @Override
    public int order() {
        return 3;
    }

    private org.apache.http.client.RedirectStrategy createRedirectStrategy() {
        DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy() {

            @Override
            protected boolean isRedirectable(String method) {
                String[] arr = CUSTOM_REDIRECT_METHODS;
                int length = arr.length;

                for(int i = 0; i < length; ++i) {
                    String m = arr[i];
                    if (m.equalsIgnoreCase(method)) {
                        return true;
                    }
                }
                return false;
            }

        };
        return defaultRedirectStrategy;
    }
}
