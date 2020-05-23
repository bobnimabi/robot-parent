package com.robot.core.task.execute;

import com.robot.code.entity.TenantRobotHead;
import com.robot.code.service.ITenantRobotHeadService;
import com.robot.core.chain.Invoker;
import com.robot.core.http.request.CustomHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 设置请求头
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class BeforeHeaderChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {
    @Autowired
    private ITenantRobotHeadService headService;

    private static final int MAX_REDIRECT = 10;

    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {
        List<TenantRobotHead> publicHeaders = headService.getPublicHeaders();
        CustomHeaders headers = result.getHeaders();
        for (TenantRobotHead head : publicHeaders) {
            Assert.hasText(head.getHeadName(), "执行前拦截：头信息:headName为空");
            Assert.hasText(head.getHeadValue(), "执行前拦截：头信息:headValue为空");
            headers.add(head.getHeadName(), head.getHeadValue());
        }
        result.setCustomEntity(params.getEntity());

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 1;
    }
}
