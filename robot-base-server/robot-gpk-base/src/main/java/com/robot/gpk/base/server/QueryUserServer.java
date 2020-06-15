package com.robot.gpk.base.server;

import com.robot.code.dto.LoginDTO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.IFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.bo.GeneralBO;
import com.robot.gpk.base.function.QueryUserFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/6/15 15:12
 * @Version 2.0
 * 登录用接口
 */
@Service
public class QueryUserServer implements IAssemFunction<String> {

    @Autowired
    private QueryUserFunction queryUserFunction;

    @Override
    public Response doFunction(ParamWrapper<String> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        return queryUserFunction.doFunction(paramWrapper, robotWrapper);
    }
}
