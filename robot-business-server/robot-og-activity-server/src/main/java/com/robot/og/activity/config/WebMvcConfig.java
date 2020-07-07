package com.robot.og.activity.config;

import com.robot.og.activity.interceptor.OGActivityTenant;
import com.robot.center.config.WebConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfig extends WebConfig {
    @Autowired
    private OGActivityTenant ogActivityTenant;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(ogActivityTenant).addPathPatterns("/**");
    }
}
