package com.robot.core.httpclient.factory;

import com.robot.code.service.IConnectionPoolConfigService;
import com.robot.core.chain.Invoker;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by mrt on 2020/3/13 15:19
 * 连接（连接池里）存活时间配置
 */
@Slf4j
@Service
public class KeepAliveChain extends BuilderFilter<Object,HttpClientBuilder> {
    @Autowired
    private IConnectionPoolConfigService poolConfigService;
    /**
     * keep-alive 超时时间，单位：秒
      */
    private static final int DEFAULT_KEEP_ALIVE = 30;

    @Override
    public void dofilter(Object params, HttpClientBuilder result, Invoker<Object, HttpClientBuilder> invoker) throws Exception {
        Integer keepAliveTimeout = Optional.of(poolConfigService.getPoolConfig().getKeepAliveTime()).orElse(DEFAULT_KEEP_ALIVE);
        result.setKeepAliveStrategy(new CustomKeepAliveStrategy(keepAliveTimeout));
        log.info("配置：KeepAlive策略{}", keepAliveTimeout);

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 4;
    }

    private class CustomKeepAliveStrategy implements ConnectionKeepAliveStrategy {
        private long keepAliveTimeout;

        private CustomKeepAliveStrategy(long keepAliveTimeout) {
            this.keepAliveTimeout = keepAliveTimeout;
        }

        /**
         * 如果Keep-Alive头里面的timeout有值，就按照该值设置连接存活时间
         * 如果没有timeout，默认设置成30秒存活
         * @param httpResponse
         * @param httpContext
         * @return
         */
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
                            return keepAliveTimeout * 1000L;
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
