package com.robot.core.task.execute;

import com.alibaba.fastjson.JSON;
import com.robot.code.service.IRequestRecordService;
import com.robot.core.chain.Invoker;
import com.robot.core.function.base.IFunctionProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请求日志记录
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Slf4j
@Service
public class BeforeLogChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {
    @Autowired
    private IRequestRecordService requestRecordService;

    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {
        requestRecordService.addRequestRecord(
                params.getRecordId(),
                params.getRobotWrapper().getId(),
                params.getPathEnum().getPathCode(),
                params.getExteralNo(),
                JSON.toJSONString(params.getEntity()));

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 4;
    }
}
