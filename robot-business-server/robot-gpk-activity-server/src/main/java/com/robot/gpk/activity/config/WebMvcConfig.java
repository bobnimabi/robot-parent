package com.robot.gpk.activity.config;

import com.robot.center.config.WebConfig;
import com.robot.gpk.activity.interceptor.TenantInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfig extends WebConfig {
    @Autowired
    private TenantInterceptor tenantInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(tenantInterceptor).addPathPatterns("/**");
    }
}
