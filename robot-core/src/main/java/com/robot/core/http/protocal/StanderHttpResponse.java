package com.robot.core.http.protocal;

import org.apache.http.*;
import java.util.Locale;

/**
 * Created by mrt on 11/1/2019 6:46 PM
 */
public class StanderHttpResponse<T> {
    private final HttpResponse original;
    private T entity;

    public StanderHttpResponse(HttpResponse httpResponse) {
        this.original = httpResponse;
    }

    public StanderHttpResponse(HttpResponse httpResponse,T entity) {
        this.original = httpResponse;
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public StatusLine getStatusLine() {
        return this.original.getStatusLine();
    }

    public Locale getLocale() {
        return this.original.getLocale();
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

    public HeaderIterator headerIterator() {
        return this.original.headerIterator();
    }

    public HeaderIterator headerIterator(String name) {
        return this.original.headerIterator(name);
    }
}
