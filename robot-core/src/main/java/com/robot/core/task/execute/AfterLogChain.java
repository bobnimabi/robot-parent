package com.robot.core.task.execute;

import com.alibaba.fastjson.JSON;
import com.robot.code.service.IRequestRecordService;
import com.robot.code.service.IResponseRecordService;
import com.robot.core.chain.Invoker;
import com.robot.core.function.base.FunctionProperty;
import com.robot.code.response.Response;
import com.robot.core.http.response.StanderHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 响应日志记录
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Slf4j
@Service
public class AfterLogChain extends ExecuteAfterFilter<StanderHttpResponse, FunctionProperty> {
    @Autowired
    private IRequestRecordService requestRecordService;

    @Autowired
    private IResponseRecordService responseRecordService;

    @Override
    public void dofilter(StanderHttpResponse params, FunctionProperty result, Invoker<StanderHttpResponse, FunctionProperty> invoker) throws Exception {
        Response response = params.getResponse();
        log.info("响应详情参见流水表：recordId：{},\r\n格式化响应体：{}", result.getRecordId(), JSON.toJSONString(response));
        requestRecordService.updateRequestRecord(result.getRecordId(), response.isSuccess(), response.getMessage());
        if (null != params.getOriginalEntity()) {
            responseRecordService.addResponseRecord(result.getRecordId(), params.getOriginalEntity(), JSON.toJSONString(response));
        }

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 3;
    }
}
