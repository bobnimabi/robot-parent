package com.robot.core.http.protocal;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by mrt on 10/22/2019 8:52 PM
 * 响应结果处理类
 * 注意：
 *  1.已经提供了最常用的响应类型的处理，html、json、byte[]
 *  2.如果有更特殊的情况（如下载），请自行定义
 */
@Slf4j
public abstract class AbstractResponseHandler implements ResponseHandler<StanderHttpResponse> {

    /**
     * 无论是正常或异常情况：源码里都已经对流进行了消费，无需手动再写一次
     * @param response
     * @return
     * @throws HttpResponseException
     * @throws IOException
     */
    @Override
    public StanderHttpResponse handleResponse(HttpResponse response) throws HttpResponseException, IOException {
        if (null == response) {
            throw new IllegalStateException("数据包：服务器未响应或被中间代理拦截");
        }
        HttpEntity httpEntity = response.getEntity();
        StatusLine statusLine = response.getStatusLine();
        if (errorStatus(statusLine)) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getStatusCode()+" "+statusLine.getReasonPhrase());
        } else if (null == httpEntity){
            StanderHttpResponse standerHttpResponse = new StanderHttpResponse(response);
            return standerHttpResponse;
        } else if (httpEntity.getContentLength() > (8 << 20)) {
            throw new IllegalArgumentException("响应大小超过8M");
        } else {
            return handleEntity(response);
        }
    }

    /**
     * 获取Charset
     * @param httpEntity
     * @return
     * null 未获取到Charset
     */
    protected Charset getCharset(HttpEntity httpEntity) {
        ContentType contentType = ContentType.get(httpEntity);
        return null != contentType ? contentType.getCharset() : null;
    }

    /**
     * 评定错误状态码
     * @return
     */
    protected abstract boolean errorStatus(StatusLine statusLine);

    /**
     * 对Entity部分进行处理
     * @param response
     * @return
     * @throws IOException
     */
    protected abstract StanderHttpResponse handleEntity(HttpResponse response) throws IOException;
}
