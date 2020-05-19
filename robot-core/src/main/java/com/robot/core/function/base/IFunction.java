package com.robot.core.function.base;

import com.bbin.common.response.ResponseResult;
import com.robot.core.robot.manager.RobotCard;

/**
 * Created by mrt on 11/15/2019 12:30 PM
 * 功能接口
 */
public interface IFunction<T> {

    /**
     * 执行功能：供特殊Function调用,或Function之间调用
     * 适用于不归还机器人的情况
     * 比如：登录、图片验证码、Function之间的调用
     * @param paramWrapper
     * @return
     * @throws Exception
     */
    /**
     * 执行功能
     * @param paramWrapper 参数包装类
     * @param robotCard 机器人令牌
     * @return
     * @throws Exception
     */
    ResponseResult doFunction(ParamWrapper<T> paramWrapper, RobotCard robotCard) throws Exception;
}
