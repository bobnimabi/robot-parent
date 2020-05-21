package com.robot.core.task.execute;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.robot.code.entity.TenantRobotHead;
import com.robot.code.entity.TenantRobotProxy;
import com.robot.code.entity.TenantRobotRequest;
import com.robot.code.service.ITenantRobotHeadService;
import com.robot.code.service.ITenantRobotProxyService;
import com.robot.code.service.ITenantRobotRequestService;
import com.bbin.utils.Validation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by mrt on 10/18/2019 10:14 AM
 */
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@Slf4j
public class CustomHttpClientConfig extends DefaultHttpClientConfig {
    @Autowired
    private ITenantRobotHeadService headService;

    @Autowired
    private ITenantRobotRequestService requestService;

    @Autowired
    private ITenantRobotProxyService proxyService;

    public CustomHttpClientConfig getCustomHttpClientConfig(long robotId) {
        CustomHttpClientConfig config = new CustomHttpClientConfig();
        fillRequestConfig(config);
        fillProxyConfig(config, robotId);
        fillHeadersConfig(config);
        return config;
    }

    private void fillRequestConfig(CustomHttpClientConfig config) {
        TenantRobotRequest request = requestService.getOne(new LambdaQueryWrapper<TenantRobotRequest>());

        if (null == request) {
            log.info("未配置request,自动使用默认配置");
            return;
        }
        if (null != request.getMaxTotal()) {
            log.info("配置：MaxTotal:{}", request.getMaxTotal());
            config.setMaxTotal(request.getMaxTotal());
        }
        if (null != request.getDefaultMaxPerRoute()) {
            log.info("配置：DefaultMaxPerRoute:{}",request.getDefaultMaxPerRoute());
            config.setDefaultMaxPerRoute(request.getDefaultMaxPerRoute());
        }
        if (null != request.getSocketTimeout()) {
            log.info("配置：SocketTimeout:{}",request.getSocketTimeout());
            config.setSocketTimeout(request.getSocketTimeout());
        }
        if (null != request.getConnectTimeout()) {
            log.info("配置：ConnectTimeout:{}",request.getConnectTimeout());
            config.setConnectTimeout(request.getConnectTimeout());
        }
        if (null != request.getConnectRequestTimeout()) {
            log.info("配置：ConnectionRequestTimeout:{}",request.getConnectRequestTimeout());
            config.setConnectionRequestTimeout(request.getConnectRequestTimeout());
        }
        Assert.isTrue(config.getMaxTotal() > 0, "MaxTotal非法");
        Assert.isTrue(config.getDefaultMaxPerRoute() > 0, "DefaultMaxPerRoute非法");
        Assert.isTrue(config.getMaxTotal() > config.getDefaultMaxPerRoute(), "MatToal应该大于等于DefaultMaxPerRoute");
        Assert.isTrue(request.getSocketTimeout() > 0, "SocketTimeOut非法");
        Assert.isTrue(request.getConnectTimeout() > 0, "ConnectTimeout非法");
        Assert.isTrue(request.getConnectRequestTimeout() > 0, "ConnectRequestTimeout非法");
    }

    private void fillProxyConfig(CustomHttpClientConfig config, long robotId) {
        TenantRobotProxy proxy = proxyService.getByRobotId(robotId);
        if (null == proxy) {
            log.info("未配置代理，将不使用代理");
            return;
        }
        Assert.hasText(config.getProxyIp(), "ProxyIp 为空");

        Assert.isTrue(InetAddressUtils.isIPv4Address(config.getProxyIp()), "ProxyIp 格式有误");
        Assert.hasText(config.getProxyPort(), "ProxyPort 为空");
        Assert.isTrue(Validation.isPort(config.getProxyPort()), "ProxyPort 格式有误");
        log.info("robotId:{},将使用代理：IP：{} PORT:{}", robotId, proxy.getProxyIp(), proxy.getProxyPort());
        config.setProxyIp(proxy.getProxyIp());
        config.setProxyPort(proxy.getProxyPort());
    }

    private void fillHeadersConfig(CustomHttpClientConfig config) {
        List<TenantRobotHead> heads = headService.list();
        if (!CollectionUtils.isEmpty(heads)) {
            CustomHeaders commonHeaders = config.getCommonHeaders();
            heads.forEach(o -> {
                log.info("配置头信息：{}->{}", o.getHeadName(), o.getHeadValue());
                commonHeaders.add(o.getHeadName(), o.getHeadValue());
            });
        } else {
            log.info("未配置头信息，不加载");
        }
    }
}
