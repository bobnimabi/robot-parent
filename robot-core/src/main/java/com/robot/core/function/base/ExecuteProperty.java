package com.robot.core.function.base;

import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.core.task.execute.IActionEnum;
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
    int rank;

    /**
     * 动作
     */
    IActionEnum action;

    /**
     * 自定义请求头
     */
    CustomHeaders headers;

    /**
     * 请求体
     */
    ICustomEntity entity;

    /**
     * 是否检查掉线
     */
    boolean isCheckLost;

    /**
     * ResultParse
     */
    IResultParse resultParse;

}
