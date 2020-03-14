package com.robot.center.httpclient;

import lombok.Data;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by mrt on 2020/3/13 15:30
 */
@Data
public class HttpClientInvocation {

    /**
     * 客户端配置参数
     */
    private HttpClientConfig config;

    /**
     * 待配置的客户端
     */
    private HttpClientBuilder httpClientBuilder;

}
