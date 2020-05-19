package com.robot.core.httpclient.factory;

import com.robot.core.chain.Invoker;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author mrt
 * @Date 2020/5/19 12:03
 * @Version 2.0
 */
public class HttpClientFactory implements IHttpClientFactory, InitializingBean {

    @Autowired
    private List<BuilderFilter> httpClientBuilderFilters;

    private Invoker invoker;

    @Override
    public CloseableHttpClient create() throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        invoker.invoke(httpClientBuilder);
        return httpClientBuilder.build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        invoker = invoker.buildInvokerChain(httpClientBuilderFilters);
    }
}
