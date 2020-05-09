package com.robot.gpk.activity.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.CommonActionEnum;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.ICustomEntity;
import com.robot.center.httpclient.JsonCustomEntity;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.gpk.base.function.LoginInFinalServer;
import com.robot.gpk.base.function.SendSmsServer;
import com.robot.gpk.base.function.ValidateSmsServer;
import com.robot.gpk.base.vo.GeneralVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginInServer extends FunctionBase<LoginDTO> {

    @Autowired
    private LoginInFinalServer loginInFinalServer;

    @Autowired
    private SendSmsServer sendSmsServer;

    @Autowired
    private ValidateSmsServer validateSmsServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        ResponseResult responseResult = loginInFinalServer.doFunction(paramWrapper, robotWrapper);
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return null;
    }

}
