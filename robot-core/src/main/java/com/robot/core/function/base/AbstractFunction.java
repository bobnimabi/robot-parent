package com.robot.core.function.base;

import com.bbin.common.response.ResponseResult;
import com.robot.core.robot.manager.RobotCard;

/**
 * @Author mrt
 * @Date 2020/5/19 13:01
 * @Version 2.0
 */
public class AbstractFunction<T> implements IFunction<T> {
    @Override
    public ResponseResult doFunction(ParamWrapper<T> paramWrapper, RobotCard robotCard) throws Exception {
        return null;
    }
}
