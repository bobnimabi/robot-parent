package com.robot.bbin.base.server;

import com.robot.bbin.base.bo.LoginData;
import com.robot.bbin.base.bo.NewResponsBo;
import com.robot.bbin.base.bo.ResponseBO;
import com.robot.bbin.base.function.LoginFunction;
import com.robot.bbin.base.function.SecondLoginFunction;
import com.robot.code.dto.LoginDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
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

    @Autowired
    private SecondLoginFunction secondLoginFunction;

    @Override
    public Response doFunction(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<NewResponsBo> newResponsbo = loginFunction.doFunction(paramWrapper, robotWrapper);
        NewResponsBo obj = newResponsbo.getObj();
        obj.setResponse_code(paramWrapper.getObj().getOpt());
        return   secondLoginFunction.doFunction(new ParamWrapper<NewResponsBo>(obj),robotWrapper);

    }
}
