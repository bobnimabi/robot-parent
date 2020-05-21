package com.robot.core.task.execute;

import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlCustomEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

/**
 * @Author mrt
 * @Date 2020/5/21 12:56
 * @Version 2.0
 */
public class ExecuteEntity {

    /**
     * httpclient
     */
    private CloseableHttpClient httpClient;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求头
     */
    private CustomHeaders headers;

    /**
     * 请求体
     */
    private ICustomEntity entity;

    /**
     * 请求上下文
     */
    private HttpContext httpContext;

    /**
     * 代理IP+端口
     */
    private String proxyIp;
    private Integer proxyPort;
}