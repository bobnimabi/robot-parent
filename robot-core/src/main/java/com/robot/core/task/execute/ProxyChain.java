package com.robot.core.task.execute;

import com.robot.core.function.base.IFunctionProperty;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class ProxyChain implements ExecuteFilter<IFunctionProperty>{
    @Override
    public boolean dofilter(IFunctionProperty invocation) throws Exception {
        return false;
    }

    @Override
    public int order() {
        return 1;
    }
}
