package com.robot.center.function;

import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;

/**
 * Created by mrt on 11/15/2019 12:30 PM
 */
public interface IFunction<T> {
    /**
     * 执行功能：供同步调用
     * @param paramWrapper
     * @return
     * @throws Exception
     */
    ResponseResult doFunction(ParamWrapper<T> paramWrapper) throws Exception;

    /**
     * 执行功能：供同步登录调用
     * @param paramWrapper
     * @return
     * @throws Exception
     */
    ResponseResult doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper) throws Exception;

    /**
     * 执行功能：异步调用
     * @param paramWrapper
     * @param robotWrapper
     * @param action
     * @param isSync 是否是同步
     * @return
     * @throws Exception
     */
    ResponseResult doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action,Boolean isSync) throws Exception;

    /**
     * 获取动作编码：异步使用
     * @return
     */
    IActionEnum getActionEnum();
}
