package com.robot.core.http.response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author mrt
 * @Date 2020/5/14 20:27
 * @Version 2.0
 * 通用JSON响应解析
 */
public class JsonResponseHandler<E> extends AbstractResponseHandler<E> {

    @Override
    protected boolean errorStatus(StatusLine statusLine) {
        int statusCode = statusLine.getStatusCode();
        return statusCode >= HttpStatus.SC_BAD_REQUEST && statusCode != HttpStatus.SC_UNAUTHORIZED;
    }

    @Override
    protected StanderHttpResponse handleEntity(HttpResponse response) throws IOException {
        Charset charset = getCharsetOrDefault(response.getEntity(), StandardCharsets.UTF_8);
        String result = EntityUtils.toString(response.getEntity(), charset);
        return new StanderHttpResponse<String, E>(response, result);
    }

    /**
     * 获取charset
     *
     * @param entity 响应entity
     * @return
     * @throws IOException
     */
    private static final Charset getCharsetOrDefault(HttpEntity entity, Charset defaultCharset) throws IOException {
        Charset charset = getCharset(entity);
        return null == charset ? defaultCharset : charset;
    }
}
