package com.robot.core.http.response;

import com.robot.code.response.Response;
import org.apache.http.*;

import java.util.Locale;

/**
 * @Author mrt
 * @Date 2020/5/19 17:26
 * @Version 2.0
 * T为原始数据类型
 * E为转换后的数据类型
 */
public class StanderHttpResponse<T,E> {
    /**
     * 原始httpResponse响应：主要获取header和statusline
     */
    private final HttpResponse originalResponse;

    /**
     * 原始响应内容，主要是字符串形式（json或html），特殊会有二进制流（图片验证码）
     */
    private T originalEntity;

    /**
     * 转换后的实体响应
     */
    private Response<E> response;


    public StanderHttpResponse(HttpResponse originalResponse) {
        this.originalResponse = originalResponse;
    }

    public StanderHttpResponse(HttpResponse httpResponse, T originalEntity) {
        this.originalResponse = httpResponse;
        this.originalEntity = originalEntity;
    }
    public T getOriginalEntity() {
        return this.originalEntity;
    }

    public Response<E> getResponse() {
        return this.response;
    }

    public void setResponse(Response<E> response) {
        this.response = response;
    }

    public StatusLine getStatusLine() {
        return this.originalResponse.getStatusLine();
    }

    public Locale getLocale() {
        return this.originalResponse.getLocale();
    }

    public ProtocolVersion getProtocolVersion() {
        return this.originalResponse.getProtocolVersion();
    }

    public boolean containsHeader(String name) {
        return this.originalResponse.containsHeader(name);
    }

    public Header[] getHeaders(String name) {
        return this.originalResponse.getHeaders(name);
    }

    public Header getFirstHeader(String name) {
        return this.originalResponse.getFirstHeader(name);
    }

    public Header getLastHeader(String name) {
        return this.originalResponse.getLastHeader(name);
    }

    public Header[] getAllHeaders() {
        return this.originalResponse.getAllHeaders();
    }

    public HeaderIterator headerIterator() {
        return this.originalResponse.headerIterator();
    }

    public HeaderIterator headerIterator(String name) {
        return this.originalResponse.headerIterator(name);
    }
}
