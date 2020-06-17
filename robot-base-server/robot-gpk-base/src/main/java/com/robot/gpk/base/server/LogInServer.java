package com.robot.gpk.base.server;

import com.robot.code.dto.LoginDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.function.LoginFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/6/15 15:12
 * @Version 2.0
 * 登录用接口
 */
@Service
public class LogInServer implements IAssemFunction<LoginDTO> {

    @Autowired
    private LoginFunction loginInFinal;

    @Override
    public Response<Object> doFunction(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        return loginInFinal.doFunction(paramWrapper, robotWrapper);
    }
}
