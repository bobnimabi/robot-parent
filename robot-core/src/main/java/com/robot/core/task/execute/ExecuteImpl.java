package com.robot.core.task.execute;

import com.robot.core.chain.Invoker;
import com.robot.core.chain.InvokerBuilder;
import com.robot.core.function.base.FunctionProperty;
import com.robot.core.http.response.StanderHttpResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author mrt
 * @Date 2020/5/23 18:46
 * @Version 2.0
 * T原始响应数据类型
 * E转换后数据类型
 */
@Service
public class ExecuteImpl<T,E> implements IExecute<T,E> , InitializingBean {
    @Autowired
    private List<ExecuteBeforeFilter<FunctionProperty, ExecuteProperty>> beforeFilters;

    @Autowired
    private List<ExecuteAfterFilter<StanderHttpResponse, FunctionProperty>> afterFilters;

    private Invoker<FunctionProperty, ExecuteProperty> beforeInvoker;

    private Invoker<StanderHttpResponse, FunctionProperty> afterInvoker;

    @Override
    public void afterPropertiesSet() throws Exception {
        beforeInvoker = InvokerBuilder.buildInvokerChain(beforeFilters);
        afterInvoker = InvokerBuilder.buildInvokerChain(afterFilters);
    }

    @Override
    public StanderHttpResponse<T,E>  request(FunctionProperty property) throws Exception {
        ExecuteProperty executeProperty = new ExecuteProperty();
        // 前拦截执行
        beforeInvoker.invoke(property,executeProperty);
        // 执行
        StanderHttpResponse<T, E> shr = HttpClientExector.execute(executeProperty);
        // 后拦截器执行
        afterInvoker.invoke(shr,property);
        return shr;
    }

}
