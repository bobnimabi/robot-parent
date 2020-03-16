package com.robot.center.httpclient.strategy;

import com.robot.center.httpclient.HttpClientFilter;
import com.robot.center.httpclient.HttpClientInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/3/13 16:52
 */
@Slf4j
@Service
public class RedirectStrategy extends HttpClientFilter<HttpClientInvocation> {

    // 可重定向的方法
    private static final String[] CUSTOM_REDIRECT_METHODS = {"GET", "HEAD","POST","DELETE","PUT"};

    @Override
    protected boolean dofilter(HttpClientInvocation invocation) throws Exception {
        HttpClientBuilder httpClientBuilder = invocation.getHttpClientBuilder();
        httpClientBuilder.setRedirectStrategy(createRedirectStrategy());
        log.info("配置：重定向：{}", CUSTOM_REDIRECT_METHODS);
        return true;
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
