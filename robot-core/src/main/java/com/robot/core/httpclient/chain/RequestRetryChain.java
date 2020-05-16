package com.robot.core.httpclient.chain;

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
public class RequestRetryChain extends HttpClientFilter<HttpClientInvocation> {

    // 重试次数
    private static final int DEFAULT_RETRY_COUNT = 3;

    @Override
    public boolean dofilter(HttpClientInvocation invocation) throws Exception {
        invocation.getHttpClientBuilder()
                .setRetryHandler(new HttpRequestRetryHandler("未定义", DEFAULT_RETRY_COUNT, true, 1000));
        log.info("配置->重试策略：加载完成");
        return true;
    }

    @Override
    public int order() {
        return 0;
    }

    private static final class HttpRequestRetryHandler extends NFHttpMethodRetryHandler {

        private final Map<String, Boolean> idempotentMethods;

        public HttpRequestRetryHandler(String httpClientName, int retryCount, boolean requestSentRetryEnabled, int sleepTimeFactorMs) {
            super(httpClientName, retryCount, requestSentRetryEnabled, sleepTimeFactorMs);
            this.idempotentMethods = new ConcurrentHashMap();
            this.idempotentMethods.put("GET", Boolean.TRUE);
            this.idempotentMethods.put("DELETE", Boolean.TRUE);
            this.idempotentMethods.put("PUT", Boolean.TRUE);
            this.idempotentMethods.put("POST", Boolean.TRUE);
        }

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            // 自定义重试业务逻辑
            boolean isRetry = (boolean) context.getAttribute("isRetry");
            if (!isRetry) {
                return false;
            }
            int num = (int) context.getAttribute("executionCount");
            // 是否重试，重试睡眠+计数
            return super.retryRequest(exception, num, context);
        }

        // 设置所有的请求方式都是幂等，是否重试由业务决定
        @Override
        protected boolean handleAsIdempotent(HttpRequest request) {
            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
            Boolean b = (Boolean)this.idempotentMethods.get(method);
            return b != null && b;
        }
    }
}
