package com.robot.og.base.server;

import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.function.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 图片验证码
 */
@Slf4j
@Service
public class ImageCodeServer implements IAssemFunction<Object> {

    @Autowired
    private ImageCodeFunction ImageCodeFunction;


    @Override
    public Response doFunction(ParamWrapper<Object> paramWrapper, RobotWrapper robotWrapper) throws Exception {

        return   ImageCodeFunction.doFunction(new ParamWrapper<Object>(paramWrapper),robotWrapper);

    }

}
