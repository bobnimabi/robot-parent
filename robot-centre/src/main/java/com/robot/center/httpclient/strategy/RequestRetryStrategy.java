package com.robot.center.httpclient.strategy;

import com.netflix.http4.NFHttpMethodRetryHandler;
import com.robot.center.httpclient.HttpClientFilter;
import com.robot.center.httpclient.HttpClientInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mrt on 2020/3/13 17:00
 */
@Slf4j
@Service
public class RequestRetryStrategy extends HttpClientFilter<HttpClientInvocation> {

    // 重试次数
    private static final int retryCount = 3;

    @Override
    protected boolean dofilter(HttpClientInvocation invocation) throws Exception {
        invocation.getHttpClientBuilder()
                .setRetryHandler(new HttpRequestRetryHandler());

        log.info("配置：Retry：retryCount:{},requestSentRetryEnabled{},sleepTimeFactorMs:{}", 1, 2, 3);

        return true;
    }

    private static final class HttpRequestRetryHandler extends NFHttpMethodRetryHandler {

        private final Map<String, Boolean> idempotentMethods;

        public HttpRequestRetryHandler(String httpClientName, int retryCount, boolean requestSentRetryEnabled, int sleepTimeFactorMs) {
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
