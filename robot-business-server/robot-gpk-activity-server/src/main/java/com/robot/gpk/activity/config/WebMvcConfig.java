package com.robot.gpk.activity.config;

import com.robot.bbin.activity.interceptor.BbinActivityTenant;
import com.robot.center.config.WebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfig extends WebConfig {

    @Autowired
    private BbinActivityTenant bbinActivityTenant;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);

        registry.addInterceptor(bbinActivityTenant).addPathPatterns("/**");
    }
}
