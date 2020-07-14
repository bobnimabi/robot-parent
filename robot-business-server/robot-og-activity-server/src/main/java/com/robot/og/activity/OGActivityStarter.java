package com.robot.og.activity;

import com.bbin.common.feignInterceptor.FeignClientInterceptor;
import com.bbin.common.feignInterceptor.FeignTenantInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

//import com.robot.center.tenant.FeignTenantInterceptor;

/**
 * Created by mrt on 11/13/2019 7:51 PM
 */
@ComponentScan(excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = {"com.bbin.common.rabbitmq.sms.*","com.bbin.common.redis.*"}),
        basePackages={
                "com.bbin.common",
                "com.robot.code",
                "com.robot.core",
                "com.robot.center",
                "com.robot.og.base",
                "com.robot.og.activity",
            }
        )
@MapperScan("com.robot.code.mapper")
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class OGActivityStarter {
    public static void main(String[] args) {
        SpringApplication.run(OGActivityStarter.class,args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

    @Bean //通过拦截器给微服务间调用前带上令牌
    public FeignClientInterceptor getFeignClientInterceptor(){
        return new FeignClientInterceptor();
    }

    @Bean// tenantId ,channelId
    public FeignTenantInterceptor getFeignTenantInterceptor(){
        return new FeignTenantInterceptor();
    }
}
