package com.robot.center.httpclient.strategy;

import com.robot.center.httpclient.HttpClientFilter;
import com.robot.center.httpclient.HttpClientInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;

/**
 * Created by mrt on 2020/3/13 17:16
 */
@Slf4j
@Service
public abstract class RequestInterceptorStrategy extends HttpClientFilter<HttpClientInvocation> {

    @Override
    protected boolean dofilter(HttpClientInvocation invocation) throws Exception {
        HttpClientBuilder httpClientBuilder = invocation.getHttpClientBuilder();
        List<HttpRequestInterceptor> interceptors = getInterceptors();
        if (!CollectionUtils.isEmpty(interceptors)) {
            interceptors.forEach(o -> httpClientBuilder.addInterceptorFirst(o));
        }
        log.info("配置：拦截器设置完成");
        return true;
    }

    /**
     * 获取连接器
     * @return
     */
    protected  abstract List<HttpRequestInterceptor> getInterceptors();

}
