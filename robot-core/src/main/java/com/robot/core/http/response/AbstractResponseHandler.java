package com.robot.core.http.response;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Created by mrt on 10/22/2019 8:52 PM
 * 响应结果处理类
 * 注意：
 *  1.已经提供了最常用的响应类型的处理，html、json、byte[]
 *  2.如果有更特殊的情况（如下载），请自行定义
 *
 *  E为未来转换后的对象
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
        if (this.errorStatus(statusLine)) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getStatusCode()+" "+statusLine.getReasonPhrase());
        } else if (null == httpEntity){
            return new StanderHttpResponse(response);
        } else if (httpEntity.getContentLength() > (8 << 20)) {
            throw new IllegalArgumentException("响应大小超过8M");
        } else {
            return this.handleEntity(response);
        }
    }

    /**
     * 评定错误状态码
     * @return
     */
    protected abstract boolean errorStatus(StatusLine statusLine);

    /**
     * 处理Entity
     * @param response
     * @return
     * @throws IOException
     */
    private StanderHttpResponse handleEntity(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        ContentType contentType = ContentType.get(entity);
        if (ContentType.TEXT_HTML.getMimeType().equalsIgnoreCase(contentType.getMimeType())) {
            byte[] bytes = EntityUtils.toByteArray(entity);
            Charset charset = CharsetHelper.getCharset(response, bytes);
            return new StanderHttpResponse(response, new String(bytes, charset));
        } else if (
                ContentType.IMAGE_GIF.getMimeType().equalsIgnoreCase(contentType.getMimeType()) ||
                        ContentType.IMAGE_JPEG.getMimeType().equalsIgnoreCase(contentType.getMimeType()) ||
                        ContentType.IMAGE_PNG.getMimeType().equalsIgnoreCase(contentType.getMimeType()) ||
                        ContentType.IMAGE_SVG.getMimeType().equalsIgnoreCase(contentType.getMimeType()) ||
                        ContentType.IMAGE_TIFF.getMimeType().equalsIgnoreCase(contentType.getMimeType()) ||
                        ContentType.IMAGE_WEBP.getMimeType().equalsIgnoreCase(contentType.getMimeType())
        ) {
            return new StanderHttpResponse(response, EntityUtils.toByteArray(entity));
        } else {
            return new StanderHttpResponse(response, EntityUtils.toString(entity, StandardCharsets.UTF_8));
        }
    }
}
