package com.robot.core.task.execute;

import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.HttpMethodEnum;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.response.StanderHttpResponse;
import lombok.Data;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

import java.net.URI;

/**
 * @Author mrt
 * @Date 2020/5/21 12:56
 * @Version 2.0
 */
@Data
public class ExecuteProperty {

    /**
     * httpclient
     */
    private CloseableHttpClient httpClient;

    /**
     * 请求路径
     */
    private URI url;

    /**
     * 请求体
     */
    private ICustomEntity customEntity;

    /**
     * 请求头
     */
    CustomHeaders headers;

    /**
     * http上下文
     */
    HttpContext httpContext;

    /**
     * 响应处理器
     */
    ResponseHandler<StanderHttpResponse> responseHandler;

    /**
     * 请求方式
     */
    HttpMethodEnum method;

    /**
     * 请求配置
     * 代理、重定向、connectionRequestTimeout、connectTimeout、socketTimeout
     */
    private RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

    /**
     * 特殊：文件上传专用
     */
    private String fileName;
    private String filePath;
}