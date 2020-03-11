package com.robot.xiaoka.income.config;

import com.robot.center.config.WebConfig;
import com.robot.xiaoka.income.interceptor.XiaoKaInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfig extends WebConfig {
    @Autowired
    private XiaoKaInterceptor tenantInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(tenantInterceptor).addPathPatterns("/**");
    }
}
