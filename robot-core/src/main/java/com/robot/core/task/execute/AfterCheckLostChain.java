package com.robot.core.task.execute;

import com.robot.code.dto.Response;
import com.robot.core.chain.Invoker;
import com.robot.core.function.base.ICheckLost;
import com.robot.core.function.base.FunctionProperty;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.IExecutorFacde;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 掉线检查
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class AfterCheckLostChain extends ExecuteAfterFilter<StanderHttpResponse, FunctionProperty> {

    @Autowired
    private IExecutorFacde functionFacde;

    @Override
    public void dofilter(StanderHttpResponse params, FunctionProperty result, Invoker<StanderHttpResponse, FunctionProperty> invoker) throws Exception {
        ICheckLost checkLost = result.getCheckLost();
        if (null == checkLost) {
            return;
        }
        RobotWrapper robotWrapper = result.getRobotWrapper();
        if (checkLost.isLose(robotWrapper,params)) {
            functionFacde.offline(robotWrapper.getId());
            params.setResponse(Response.FAIL("机器人掉线,请重新登录：robotID:"+robotWrapper.getId()));
        }

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 1;
    }
}
