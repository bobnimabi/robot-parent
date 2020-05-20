package com.robot.core.task.execute;

import com.robot.code.service.ITenantRobotProxyService;
import com.robot.core.function.base.IFunctionProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class ProxyPreChain extends ExecutePreFilter<IFunctionProperty> {
    @Autowired
    private ITenantRobotProxyService proxyService;

    @Override
    public boolean dofilter(IFunctionProperty invocation) throws Exception {

        return false;
    }

    @Override
    public int order() {
        return 1;
    }
}
