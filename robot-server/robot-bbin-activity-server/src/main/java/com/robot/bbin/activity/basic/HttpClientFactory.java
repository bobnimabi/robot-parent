package com.robot.bbin.activity.basic;

import com.robot.center.httpclient.AbstractHttpClientFactory;
import org.apache.http.HttpRequestInterceptor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mrt on 11/18/2019 12:45 PM
 */
@Service
public class HttpClientFactory extends AbstractHttpClientFactory {
    // 暂时不需要请求拦截器
    @Override
    protected List<HttpRequestInterceptor> getRequestInterceptor() {
        return null;
    }
}
