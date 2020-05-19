package com.robot.core.function.base;

import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;

/**
 * 检查是否掉线
 * @Author mrt
 * @Date 2020/5/19 15:46
 * @Version 2.0
 */
public interface ICheckLost {
    boolean isLose(RobotWrapper robotWrapper, StanderHttpResponse standerHttpResponse);
}
