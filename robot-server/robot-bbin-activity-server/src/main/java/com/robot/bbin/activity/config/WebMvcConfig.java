package com.robot.bbin.activity.config;

import com.robot.bbin.activity.interceptor.TenantInterceptor;
import com.robot.center.project.config.WebConfig;
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
