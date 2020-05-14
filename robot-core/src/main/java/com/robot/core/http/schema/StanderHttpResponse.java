package com.robot.core.http.schema;

import com.bbin.common.response.ResponseResult;
import lombok.Data;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.execchain.ConnectionHolder;
import org.apache.http.impl.execchain.ResponseEntityProxy;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by mrt on 11/1/2019 6:46 PM
 */
public class StanderHttpResponse<T> extends implements CloseableHttpResponse {
    // 状态行
    private StatusLine statusLine;
    // 响应头
    private Header[] headers;
    // 二进制响应体
    private byte[] entity;
    // 响应体（String）
    private String entityStr;
    // 转换后的响应体
    private ResponseResult responseResult;
    private String recordId;


    private final HttpResponse original;


    public StanderHttpResponse(HttpResponse original) {
        this.original = original;
    }
    public StatusLine getStatusLine() {
        return this.original.getStatusLine();
    }

    public void setStatusLine(StatusLine statusline) {
        this.original.setStatusLine(statusline);
    }

    public void setStatusLine(ProtocolVersion ver, int code) {
        this.original.setStatusLine(ver, code);
    }

    public void setStatusLine(ProtocolVersion ver, int code, String reason) {
        this.original.setStatusLine(ver, code, reason);
    }

    public void setStatusCode(int code) throws IllegalStateException {
        this.original.setStatusCode(code);
    }

    public void setReasonPhrase(String reason) throws IllegalStateException {
        this.original.setReasonPhrase(reason);
    }

    public HttpEntity getEntity() {
        return this.original.getEntity();
    }

    public void setEntity(HttpEntity entity) {
        this.original.setEntity(entity);
    }

    public Locale getLocale() {
        return this.original.getLocale();
    }

    public void setLocale(Locale loc) {
        this.original.setLocale(loc);
    }

    public ProtocolVersion getProtocolVersion() {
        return this.original.getProtocolVersion();
    }

    public boolean containsHeader(String name) {
        return this.original.containsHeader(name);
    }

    public Header[] getHeaders(String name) {
        return this.original.getHeaders(name);
    }

    public Header getFirstHeader(String name) {
        return this.original.getFirstHeader(name);
    }

    public Header getLastHeader(String name) {
        return this.original.getLastHeader(name);
    }

    public Header[] getAllHeaders() {
        return this.original.getAllHeaders();
    }

    public void addHeader(Header header) {
        this.original.addHeader(header);
    }

    public void addHeader(String name, String value) {
        this.original.addHeader(name, value);
    }

    public void setHeader(Header header) {
        this.original.setHeader(header);
    }

    public void setHeader(String name, String value) {
        this.original.setHeader(name, value);
    }

    public void setHeaders(Header[] headers) {
        this.original.setHeaders(headers);
    }

    public void removeHeader(Header header) {
        this.original.removeHeader(header);
    }

    public void removeHeaders(String name) {
        this.original.removeHeaders(name);
    }

    public HeaderIterator headerIterator() {
        return this.original.headerIterator();
    }

    public HeaderIterator headerIterator(String name) {
        return this.original.headerIterator(name);
    }

    @Override
    public HttpParams getParams() {
        return null;
    }

    @Override
    public void setParams(HttpParams httpParams) {

    }


    public String toString() {
        StringBuilder sb = new StringBuilder("HttpResponseProxy{");
        sb.append(this.original);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void close() throws IOException {

    }
}
