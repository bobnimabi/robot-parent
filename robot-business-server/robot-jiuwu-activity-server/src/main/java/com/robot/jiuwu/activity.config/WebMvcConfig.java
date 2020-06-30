package com.robot.jiuwu.activity.config;

import com.robot.center.config.WebConfig;
import com.robot.jiuwu.activity.interceptor.JiuWuActivityTenant;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfig extends WebConfig {
    @Autowired
    private JiuWuActivityTenant jiuWuActivityTenant;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(jiuWuActivityTenant).addPathPatterns("/**");
    }
}
