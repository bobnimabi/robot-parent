package com.robot.core.http.response;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

/**
 * @Author mrt
 * @Date 2020/6/15 10:40
 * @Version 2.0
 */
public class CustomResponseHandler extends AbstractResponseHandler {
    /**
     * 饿汉单例
     */
    public static final CustomResponseHandler RESPONSE_HANDLER = new CustomResponseHandler();

    @Override
    protected boolean errorStatus(StatusLine statusLine) {
        int statusCode = statusLine.getStatusCode();
        return statusCode >= HttpStatus.SC_BAD_REQUEST && statusCode != HttpStatus.SC_UNAUTHORIZED;
    }
}
