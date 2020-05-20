package com.robot.core.function.base;

import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author mrt
 * @Date 2020/5/19 19:34
 * @Version 2.0
 */
@Data
@AllArgsConstructor
public class ExecuteProperty implements IFunctionProperty{

    /**
     * 域名等级
     */
    private int rank;

    /**
     * 动作
     */
    private IActionEnum action;

    /**
     * 自定义请求头
     */
    private CustomHeaders headers;

    /**
     * 请求体
     */
    private ICustomEntity entity;

    /**
     * 是否检查掉线
     */
    private boolean isCheckLost;

    /**
     * ResultParse
     */
    private IResultParse resultParse;

    /**
     * 机器人包装类
     */
    private RobotWrapper robotWrapper;

}
