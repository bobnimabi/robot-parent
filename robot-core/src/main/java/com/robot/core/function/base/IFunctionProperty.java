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
     * 获取域名等级
     * @return
     */
    int getRank();

    /**
     * 获取动作
     * @return
     */
    IActionEnum getAction();

    /**
     * 获取接口特定请求头
     * 注意：可以对公共头进行覆盖（tenant_robot_header表配置）
     * 有些特定的登录的token会存在于cookie的属性里面
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
    IResultParse getResultParse();

    /**
     * 机器人包装类
     */
    RobotWrapper getRobotWrapper();

    /**
     * 获取外部订单号
     */
    String getOutNo();

    /**
     * 流水id
     */
    long getRecordId();
}
