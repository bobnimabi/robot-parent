package com.robot.core.http.response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Author mrt
 * @Date 2020/5/14 20:27
 * @Version 2.0
 * 通用Byte[]响应解析
 */
public class ByteResponseHandler extends AbstractResponseHandler {

    /**
     * 饿汉单例
     */
    public static final ByteResponseHandler BYTE_RESPONSE_HANDLER = new ByteResponseHandler();

    @Override
    protected boolean errorStatus(StatusLine statusLine) {
        int statusCode = statusLine.getStatusCode();
        return statusCode >= HttpStatus.SC_BAD_REQUEST && statusCode != HttpStatus.SC_UNAUTHORIZED;
    }

    @Override
    protected StanderHttpResponse handleEntity(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        byte[] bytes = EntityUtils.toByteArray(response.getEntity());
        return new StanderHttpResponse(response, bytes);
    }
}
