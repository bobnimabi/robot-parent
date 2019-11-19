package com.robot.center.httpclient;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by mrt on 10/20/2019 5:00 PM
 */
public class CustomInterceptorSample implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        String authorization = (String)httpContext.getAttribute("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            throw new RuntimeException("Authorization为空");
        }
        httpRequest.addHeader("Authorization", authorization);
    }
}
