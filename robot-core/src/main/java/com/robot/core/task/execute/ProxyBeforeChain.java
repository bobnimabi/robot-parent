package com.robot.core.task.execute;

import com.robot.code.entity.TenantRobotProxy;
import com.robot.code.service.ITenantRobotProxyService;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class ProxyBeforeChain extends ExecuteBeforeFilter<IFunctionProperty> {

    @Autowired
    private ITenantRobotProxyService proxyService;

    @Override
    public boolean dofilter(IFunctionProperty params) throws Exception {
        Long robotId = params.getRobotWrapper().getId();
        TenantRobotProxy proxy = proxyService.getProxy(robotId);
        if (null != proxy) {
            String ip = proxy.getProxyIp();
            Integer port = proxy.getProxyPort();
            Assert.hasText(ip, "Proxy:Ip 为空,robotID："+robotId);
            Assert.notNull(port, "Proxy:Port 为空,robotID："+robotId);
            Assert.isTrue(InetAddressUtils.isIPv4Address(proxy.getProxyIp()), "Proxy:Ip 格式有误,robotID："+robotId);
            Assert.isTrue(port>=0 && port<=65535,"Proxy:Port 不在0-65535范围内,robotID："+robotId);


            RequestConfig.Builder custom = RequestConfig.custom();
            custom.setProxy(new HttpHost(proxy.getProxyIp(), proxy.getProxyPort()));
        }
        return true;
    }

    @Override
    public int order() {
        return 1;
    }
}
