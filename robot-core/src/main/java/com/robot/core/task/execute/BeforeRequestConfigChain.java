package com.robot.core.task.execute;

import com.robot.code.entity.HttpRequestConfig;
import com.robot.code.entity.TenantRobotPath;
import com.robot.code.entity.TenantRobotProxy;
import com.robot.code.service.IHttpRequestConfigService;
import com.robot.code.service.ITenantRobotPathService;
import com.robot.code.service.ITenantRobotProxyService;
import com.robot.core.chain.Invoker;
import com.robot.core.common.HttpConsts;
import com.robot.core.function.base.IFunctionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 接口请求相关参数：CookieStore、重试、重定向、获取连接超时、请求超时、响应超时
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Slf4j
@Service
public class BeforeRequestConfigChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {
    @Autowired
    private ITenantRobotPathService pathService;

    @Autowired
    private IHttpRequestConfigService configService;

    @Autowired
    private ITenantRobotProxyService proxyService;

    /**
     * 最大重定向次数
     */
    private static final int MAX_REDIRECT = 10;

    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {
        TenantRobotPath path = pathService.getPath(params.getAction().getActionCode());

        // requestConfigId
        Long requestConfigId = path.getHttpRequestConfigId();
        if (null != requestConfigId) {
            HttpRequestConfig config = configService.getConfigById(requestConfigId);
            // 设置重试
            setRetry(config, result);
            // 设置重定向
            setRedirect(config, result);
            // 设置各种超时
            setTimeout(config, result);
        } else {
            log.info("未指定RequestConfig,请检查...");
        }

        // 设置代理
        setProxy(params, result);

        invoker.invoke(params, result);
    }

    /**
     * 设置重试
     * @param config
     * @param result
     */
    private void setRetry(HttpRequestConfig config, ExecuteProperty result) {
        HttpContext httpContext = result.getHttpContext();
        httpContext.setAttribute(HttpConsts.RETRY_FLAG, config.getIsRetry());
        log.info("请求拦截：重试：{}", config.getIsRetry());
    }

    /**
     * 设置重定向
     * @param config
     * @param result
     */
    private void setRedirect(HttpRequestConfig config, ExecuteProperty result) {
        RequestConfig.Builder requestConfigBuilder = result.getRequestConfigBuilder();
        requestConfigBuilder.setRedirectsEnabled(config.getIsRedirect());
        requestConfigBuilder.setRelativeRedirectsAllowed(config.getIsRedirect()); // 5.0版本取消了该属性
        requestConfigBuilder.setCircularRedirectsAllowed(false);
        requestConfigBuilder.setMaxRedirects(MAX_REDIRECT);
        if (config.getIsRedirect()) {
            log.info("请求拦截：重定向:开启");
        }
    }

    /**
     * 设置各种超时
     * @param config
     * @param result
     */
    private void setTimeout(HttpRequestConfig config, ExecuteProperty result) {
        RequestConfig.Builder builder = result.getRequestConfigBuilder();
        builder.setConnectionRequestTimeout(config.getConnectRequestTimeout());
        builder.setConnectTimeout(config.getConnectTimeout());
        // 5.0版本将ResponseTimeOut加上，现在暂时暂时
    }

    /**
     * 设置代理
     * @param params
     * @param result
     */
    private void setProxy(IFunctionProperty params, ExecuteProperty result) {
        Long robotId = params.getRobotWrapper().getId();
        TenantRobotProxy proxy = proxyService.getProxy(robotId);
        if (null != proxy) {
            String ip = proxy.getProxyIp();
            Integer port = proxy.getProxyPort();
            Assert.hasText(ip, "执行前拦截：Proxy:Ip 为空,robotID："+robotId);
            Assert.notNull(port, "执行前拦截：Proxy:Port 为空,robotID："+robotId);
            Assert.isTrue(InetAddressUtils.isIPv4Address(proxy.getProxyIp()), "执行前拦截：Proxy:Ip 格式有误,robotID："+robotId);
            Assert.isTrue(port>=0 && port<=65535,"执行前拦截：Proxy:Port 不在0-65535范围内,robotID："+robotId);

            RequestConfig.Builder builder = result.getRequestConfigBuilder();
            builder.setProxy(new HttpHost(proxy.getProxyIp(), proxy.getProxyPort()));
            log.info("执行前拦截：使用代理：IP：{},PORT:{}", ip, port);
        }
    }

    @Override
    public int order() {
        return 3;
    }
}
