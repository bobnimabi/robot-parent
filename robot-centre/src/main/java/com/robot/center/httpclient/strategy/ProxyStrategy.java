package com.robot.center.httpclient.strategy;

import com.robot.center.httpclient.HttpClientFilter;
import com.robot.center.httpclient.HttpClientInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 2020/3/13 17:10
 */
@Slf4j
@Service
public class ProxyStrategy extends HttpClientFilter<HttpClientInvocation> {

    @Override
    protected boolean dofilter(HttpClientInvocation invocation) throws Exception {
        // 设置代理
        if (!StringUtils.isEmpty(config.getProxyIp()) && !StringUtils.isEmpty(config.getProxyPort())) {
            httpClientBuilder.setProxy(new HttpHost(config.getProxyIp(), Integer.parseInt(config.getProxyPort())));
            log.info("配置：代理：IP:{},PORT:{}", ip, port);
        } else {
            log.info("配置：代理：IP和PORT无需配置");
        }
        return true;
    }

}
