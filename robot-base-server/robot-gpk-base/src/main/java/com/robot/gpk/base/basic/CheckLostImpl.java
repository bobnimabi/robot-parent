package com.robot.gpk.base.basic;

import com.robot.core.function.base.ICheckLost;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.stereotype.Service;

/**
 * 全局通用：检查是否掉线
 * @Author mrt
 * @Date 2020/6/1 13:06
 * @Version 2.0
 * 注意：类名不要改变
 */
@Service
public class CheckLostImpl implements ICheckLost {

    @Override
    public boolean isLose(RobotWrapper robotWrapper, StanderHttpResponse standerHttpResponse) {
        return false;
    }
}
