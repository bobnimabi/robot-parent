package com.robot.core.task.execute;

import com.robot.core.chain.Invoker;
import com.robot.core.function.base.ICheckLost;
import com.robot.core.function.base.Response;
import com.robot.core.http.response.StanderHttpResponse;
import org.springframework.stereotype.Service;

/**
 * 响应原始结果转对象
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class AfterParseChain extends ExecuteAfterFilter<StanderHttpResponse,IFunctionProperty> {

    @Override
    public void dofilter(StanderHttpResponse params, IFunctionProperty result, Invoker<StanderHttpResponse, IFunctionProperty> invoker) throws Exception {
        // 请求结果处理
        Response response = result.getResultParse().parse2Obj(params);
        params.setResponse(response);
    }

    @Override
    public int order() {
        return 1;
    }
}
