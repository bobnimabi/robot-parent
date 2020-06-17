package com.robot.core.task.dispatcher;

import com.robot.core.common.TContext;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.IDispatcherFacde;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/6/17 10:26
 * @Version 2.0
 */
@Service
public class AsyncTask {
    @Autowired
    protected IDispatcherFacde dispatcherFacde;
    @Autowired
    private AssumFunctionHelper assumFunctionHelper;

    @Async
    public void doAssumFunction(ParamWrapper paramWrapper, IFunctionEnum functionEnum, RobotWrapper robotWrapper, RegisterBody registerBody) throws Exception {
        try {
            AsyncSelector.handTenant(registerBody);
            IAssemFunction iFunction = assumFunctionHelper.getFunction(functionEnum);
            iFunction.doFunction(paramWrapper, robotWrapper);
        }finally {
            try{
                dispatcherFacde.giveBackCookieAndToken(robotWrapper);
            }finally {
                TContext.clean();
            }
        }
    }
}
