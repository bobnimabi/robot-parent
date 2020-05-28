package com.robot.core.robot.manager;

import com.robot.code.dto.Response;
import com.robot.code.dto.TenantRobotDTO;

/**
 * @Author mrt
 * @Date 2020/5/27 12:06
 * @Version 2.0
 */
public interface IFunctionFacde {

    /**
     * 上线机器人
     * @param robotWrapper
     * @return
     */
    boolean online(RobotWrapper robotWrapper);

    /**
     * 下线机器人
     * @param robotId
     * @return
     */
    boolean offline(long robotId);
}
