package com.robot.gpk.base.basic;

import com.robot.center.httpclient.AbstractHttpClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequestInterceptor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by mrt on 11/18/2019 12:45 PM
 * 注意：
 */
@Slf4j
@Service
public class HttpClientFactory extends AbstractHttpClientFactory {

    @Override
    protected List<HttpRequestInterceptor> getRequestInterceptor() {
        return null;
    }
}
