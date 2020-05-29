package com.robot.center.function;

import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IPathEnum;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;

/**
 * Created by mrt on 11/15/2019 12:30 PM
 */
public interface IFunction<T> {
    /**
     * 执行功能：供普通Function调用
     * 会自动归还机器人
     * @param paramWrapper
     * @return
     * @throws Exception
     */
    ResponseResult doFunction(ParamWrapper<T> paramWrapper) throws Exception;

    /**
     * 执行功能：供特殊Function调用,或Function之间调用
     * 适用于不归还机器人的情况
     * 比如：登录、图片验证码、Function之间的调用
     * @param paramWrapper
     * @return
     * @throws Exception
     */
    ResponseResult doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper) throws Exception;

    /**
     * 执行功能
     * @param paramWrapper
     * @param robotWrapper
     * @param action
     * @param isGiveBack 是否是同步
     * @return
     * @throws Exception
     */
    ResponseResult doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action,Boolean isGiveBack) throws Exception;

    /**
     * 获取Action
     * @return
     */
    IPathEnum getActionEnum();

}
