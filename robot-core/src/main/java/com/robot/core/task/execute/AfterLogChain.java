package com.robot.core.task.execute;

import com.alibaba.fastjson.JSON;
import com.robot.code.service.IRequestRecordService;
import com.robot.code.service.IResponseRecordService;
import com.robot.core.chain.Invoker;
import com.robot.core.function.base.IFunctionProperty;
import com.robot.core.function.base.Response;
import com.robot.core.http.response.StanderHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 响应日志记录
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class AfterLogChain extends ExecuteAfterFilter<StanderHttpResponse, IFunctionProperty> {
    @Autowired
    private IRequestRecordService requestRecordService;

    @Autowired
    private IResponseRecordService responseRecordService;

    @Override
    public void dofilter(StanderHttpResponse params, IFunctionProperty result, Invoker<StanderHttpResponse, IFunctionProperty> invoker) throws Exception {
        Response response = params.getResponse();
        requestRecordService.updateRequestRecord(result.getRecordId(), response.isSuccess(), response.getMessage());
        if (response.isSuccess() && null != response.getObj()) {
            responseRecordService.addResponseRecord(result.getRecordId(), params.getOriginalEntity(), JSON.toJSONString(response.getObj()));
        }
    }

    @Override
    public int order() {
        return 2;
    }
}
