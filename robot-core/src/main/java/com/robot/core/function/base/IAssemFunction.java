package com.robot.core.function.base;

import com.robot.code.dto.Response;
import com.robot.core.robot.manager.RobotWrapper;

/**
 * Created by mrt on 11/15/2019 12:30 PM
 * 组装功能接口
 * T参数对象类型：
 */
public interface IAssemFunction<T> {

    /**
     * 执行功能
     *
     * @param paramWrapper 参数包装类
     * @param robotWrapper 机器人
     * @return
     * @throws Exception
     */
    Response doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper) throws Exception;
}
