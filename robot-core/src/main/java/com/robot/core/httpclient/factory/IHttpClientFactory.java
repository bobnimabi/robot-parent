package com.robot.core.httpclient.factory;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @Author mrt
 * @Date 2020/5/19 12:02
 * @Version 2.0
 */
public interface IHttpClientFactory {
    CloseableHttpClient create() throws Exception;
}
