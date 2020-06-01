package com.robot.core.task.execute;

import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.MethodEnum;
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
     * 请求方式
     */
    private MethodEnum method;

    /**
     * 请求头
     */
    private CustomHeaders headers;

    /**
     * 请求体
     */
    private ICustomEntity customEntity;

    /**
     * http上下文
     */
    private HttpContext httpContext;

    /**
     * 请求配置
     */
    private RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

    /**
     * http响应处理器
     */
    private ResponseHandler<StanderHttpResponse> responseHandler;

    /**
     * 特殊：文件上传专用,暂时不用
     */
    private String fileName;
    private String filePath;
}