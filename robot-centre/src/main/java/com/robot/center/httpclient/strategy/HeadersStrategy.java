package com.robot.center.httpclient.strategy;

import com.alibaba.fastjson.JSON;
import com.robot.center.httpclient.HttpClientFilter;
import com.robot.center.httpclient.HttpClientInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by mrt on 2020/3/13 16:17
 */
@Slf4j
@Service
public class HeadersStrategy extends HttpClientFilter<HttpClientInvocation> {

    @Override
    protected boolean dofilter(HttpClientInvocation invocation) throws Exception {
        RequestConfig
        List<Header> headers = invocation.getConfig().getCommonHeaders().getHeaders();
        HttpClientBuilder httpClientBuilder = invocation.getHttpClientBuilder();
        httpClientBuilder.setDefaultHeaders(headers);
        log.info("配置：Headers：{}", JSON.toJSONString(headers));
        return true;
    }
}
