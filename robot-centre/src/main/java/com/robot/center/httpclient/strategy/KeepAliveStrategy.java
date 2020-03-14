package com.robot.center.httpclient.strategy;

import com.robot.center.httpclient.HttpClientFilter;
import com.robot.center.httpclient.HttpClientInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/3/13 15:19
 * 连接（连接池里）存活时间配置
 */
@Slf4j
@Service
public class KeepAliveStrategy extends HttpClientFilter<HttpClientInvocation> {
    // keep-alive 超时时间，单位：秒
    private static final int DEFAULT_KEEP_ALIVE = 30;

    @Override
    protected boolean dofilter(HttpClientInvocation invocation) throws Exception {
        int keepAliveTimeout = invocation.getConfig().getKeepAliveTimeout();
        HttpClientBuilder httpClientBuilder = invocation.getHttpClientBuilder();
        httpClientBuilder.setKeepAliveStrategy(new CustomKeepAliveStrategy(keepAliveTimeout));
        log.info("配置：KeepAliveTimeOut:{}", keepAliveTimeout);
        return true;
    }

    private class CustomKeepAliveStrategy implements ConnectionKeepAliveStrategy {
        private long keepAliveTimeout;

        private CustomKeepAliveStrategy(long keepAliveTimeout) {
            this.keepAliveTimeout = keepAliveTimeout;
        }

        @Override
        public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
            Args.notNull(httpResponse, "HTTP response");
            BasicHeaderElementIterator it = new BasicHeaderElementIterator(httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (true) {
                String param;
                String value;
                do {
                    do {
                        if (!it.hasNext()) {
                            return DEFAULT_KEEP_ALIVE * 1000L;
                        }
                        HeaderElement he = it.nextElement();
                        param = he.getName();
                        value = he.getValue();
                    } while (value == null);
                } while (!param.equalsIgnoreCase("timeout"));

                try {
                    return Long.parseLong(value) * 1000L;
                } catch (NumberFormatException e) {
                }
            }
        }
    }
}
