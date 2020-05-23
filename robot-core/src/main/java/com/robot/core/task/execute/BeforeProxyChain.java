package com.robot.core.task.execute;

import com.robot.code.entity.TenantRobotProxy;
import com.robot.code.service.ITenantRobotProxyService;
import com.robot.core.chain.Invoker;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 设置代理
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class BeforeProxyChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {

    @Autowired
    private ITenantRobotProxyService proxyService;

    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {
        Long robotId = params.getRobotWrapper().getId();
        TenantRobotProxy proxy = proxyService.getProxy(robotId);
        if (null != proxy) {
            String ip = proxy.getProxyIp();
            Integer port = proxy.getProxyPort();
            Assert.hasText(ip, "执行前拦截：Proxy:Ip 为空,robotID："+robotId);
            Assert.notNull(port, "执行前拦截：Proxy:Port 为空,robotID："+robotId);
            Assert.isTrue(InetAddressUtils.isIPv4Address(proxy.getProxyIp()), "执行前拦截：Proxy:Ip 格式有误,robotID："+robotId);
            Assert.isTrue(port>=0 && port<=65535,"执行前拦截：Proxy:Port 不在0-65535范围内,robotID："+robotId);

            RequestConfig.Builder builder = result.getRequestConfig();
            builder.setProxy(new HttpHost(proxy.getProxyIp(), proxy.getProxyPort()));
        }
        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 1;
    }
}
