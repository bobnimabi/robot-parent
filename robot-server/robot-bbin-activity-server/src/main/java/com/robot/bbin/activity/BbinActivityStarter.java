package com.robot.bbin.activity;

import com.bbin.common.interceptor.FeignClientInterceptor;
import com.robot.center.tenant.FeignTenantInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * Created by mrt on 11/13/2019 7:51 PM
 */
@ComponentScan(basePackages = {
        "com.robot.code",
        "com.robot.center",
        "com.robot.bbin.activity",
    }
)
@MapperScan("com.robot.code.mapper")
@SpringBootApplication
public class BbinActivityStarter {
    public static void main(String[] args) {
        SpringApplication.run(BbinActivityStarter.class,args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

    @Bean//通过拦截器给微服务间调用前带上令牌
    public FeignClientInterceptor getFeignClientInterceptor(){
        return new FeignClientInterceptor();
    }

    @Bean// tenantId ,channelId
    public FeignTenantInterceptor getFeignTenantInterceptor(){
        return new FeignTenantInterceptor();
    }
}
