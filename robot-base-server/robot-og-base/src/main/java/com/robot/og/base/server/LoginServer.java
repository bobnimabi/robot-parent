package com.robot.og.base.server;


import com.robot.code.dto.LoginDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.function.LoginFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录
 * @Author mrt
 * @Date 2020/6/15 15:12
 * @Version 2.0
 */
@Service
public class LoginServer implements IAssemFunction<LoginDTO> {


    @Autowired
    private LoginFunction loginFunction;

    @Override
    public Response doFunction(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        return loginFunction.doFunction(paramWrapper, robotWrapper);

    }
}
