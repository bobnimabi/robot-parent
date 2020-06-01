package com.robot.core.function.base;

import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.robot.manager.RobotWrapper;

/**
 * Created by mrt on 11/15/2019 12:30 PM
 * 功能接口
 */
public interface IFunctionProperty {

    /**
     * 获取动作
     * @return
     */
    IPathEnum getPathEnum();

    /**
     * 获取接口特定请求头
     * 注意：可对公共头进行覆盖
     * @return
     */
    CustomHeaders getHeaders();

    /**
     * 获取请求体
     * @return
     */
    ICustomEntity getEntity();

    /**
     * 是否检查掉线
     * @return
     */
    ICheckLost getCheckLost();

    /**
     * 获取ResultParse
     * @return
     */
    IResultHandler getResultHandler();

    /**
     * 机器人包装类
     */
    RobotWrapper getRobotWrapper();

    /**
     * 获取外部订单号
     */
    String getExteralNo();

    /**
     * 流水id
     */
    long getRecordId();
}
