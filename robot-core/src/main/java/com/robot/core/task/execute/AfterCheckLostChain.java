package com.robot.core.task.execute;

import com.robot.core.chain.Invoker;
import com.robot.core.function.base.ICheckLost;
import com.robot.core.function.base.IFunctionProperty;
import com.robot.core.http.response.StanderHttpResponse;
import org.springframework.stereotype.Service;

/**
 * 掉线检查
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class AfterCheckLostChain extends ExecuteAfterFilter<StanderHttpResponse, IFunctionProperty> {

    // TODO 进入机器人管理

    @Override
    public void dofilter(StanderHttpResponse params, IFunctionProperty result, Invoker<StanderHttpResponse, IFunctionProperty> invoker) throws Exception {
        ICheckLost checkLost = result.getCheckLost();
        if (null == checkLost) {
            return;
        }
        if (checkLost.isLose(result.getRobotWrapper(),params)) {
            // TODO 检查掉线的话，掉线要关掉机器人

        }
    }

    @Override
    public int order() {
        return 3;
    }
}
