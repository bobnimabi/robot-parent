package com.robot.center.httpclient;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

/**
 * Created by mrt on 10/17/2019 8:26 PM
 */
@Slf4j
@Data
public abstract class AbstractHttpClientFactory extends HttpClientFactoryBase{

    /**
     * 创建定制化httpclient
     * @param robotId
     * @return
     */
    public CloseableHttpClient createHttpClient(long robotId)  {
        try {
            return createCustomHttpClient(robotId, getRequestInterceptor());
        } catch (Exception e) {
            log.info("创建client异常", e);
        }
        return null;
    }

    /**
     * 获取请求拦截器
     * @return
     */
    protected abstract List<HttpRequestInterceptor> getRequestInterceptor();
}
