package com.robot.gpk.activity.function;

import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.gpk.base.function.LoginInFinalServer;
import com.robot.gpk.base.function.SendSmsServer;
import com.robot.gpk.base.function.ValidateSmsServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginInPreServer extends FunctionBase<LoginDTO> {

    @Autowired
    private LoginInFinalServer loginInFinalServer;

    @Autowired
    private SendSmsServer sendSmsServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
//        loginInFinalServer.doFunction()
//        ResponseResult responseResult = validateSmsServer.doFunction(paramWrapper, robotWrapper);
//        return responseResult;
        return null;
    }

    @Override
    public IActionEnum getActionEnum() {
        return null;
    }

}
