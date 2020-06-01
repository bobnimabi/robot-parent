package com.robot.bbin.base.basic;

import com.robot.core.function.base.ICheckLost;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;

/**
 * @Author mrt
 * @Date 2020/6/1 13:06
 * @Version 2.0
 */
public class CheckLostImpl implements ICheckLost {

    @Override
    public boolean isLose(RobotWrapper robotWrapper, StanderHttpResponse standerHttpResponse) {
        return false;
    }
}
