package com.robot.core.task.execute;

import com.robot.code.entity.TenantRobotProxy;
import com.robot.code.service.ITenantRobotProxyService;
import com.robot.core.function.base.IFunctionProperty;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean dofilter(IFunctionProperty property) throws Exception {
        TenantRobotProxy proxy = proxyService.getProxy(property.getRobotWrapper().getId());
        RequestConfig.Builder custom = RequestConfig.custom();
        custom.setProxy(new HttpHost(proxy.getProxyIp(), proxy.getProxyPort()));

        return false;
    }

    @Override
    public int order() {
        return 1;
    }
}
