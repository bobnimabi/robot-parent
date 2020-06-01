package com.robot.core.task.execute;

import com.robot.core.chain.Invoker;
import com.robot.core.function.base.IFunctionProperty;
import com.robot.code.dto.Response;
import com.robot.core.http.response.StanderHttpResponse;
import org.springframework.stereotype.Service;

/**
 * 响应原始结果转对象
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class AfterParseChain extends ExecuteAfterFilter<StanderHttpResponse, IFunctionProperty> {

    @Override
    public void dofilter(StanderHttpResponse params, IFunctionProperty result, Invoker<StanderHttpResponse, IFunctionProperty> invoker) throws Exception {
        // 如果掉线检查，发现掉线，就不对结果进行处理了
        Response checkResp = params.getResponse();
        boolean isLost = null != checkResp && !checkResp.isSuccess();
        if (!isLost) {
            Response response = result.getResultHandler().parse2Obj(params);
            params.setResponse(response);
        }

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 2;
    }
}
