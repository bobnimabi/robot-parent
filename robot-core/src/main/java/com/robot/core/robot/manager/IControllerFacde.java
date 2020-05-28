package com.robot.core.robot.manager;

import com.robot.code.dto.Response;
import com.robot.code.dto.TenantRobotDTO;

/**
 * @Author mrt
 * @Date 2020/5/27 12:06
 * @Version 2.0
 */
public interface IControllerFacde {
    /**
     * 增加机器人
     * @param robotDTO
     * @return
     */
    Response addRobot(TenantRobotDTO robotDTO);

    /**
     * 删除机器人
     * @param robotId
     * @return
     */
    Response deleteRobot(long robotId);

    /**
     * 更新机器人
     * @param robotDTO
     * @return
     */
    Response updateRobot(TenantRobotDTO robotDTO);

    /**
     * 页面展示机器人
     * @param robotDTO
     * @return
     */
    Response pageRobot(TenantRobotDTO robotDTO);

    /**
     * 通过id获取机器人
     * @param robotId
     * @return
     */
    Response getRobotById(long robotId);

    /**
     * 下线机器人
     * @param robotId
     * @return
     */
    boolean offline(long robotId);
}
