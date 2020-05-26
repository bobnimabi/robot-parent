package com.robot.core.robot.manager;

import com.robot.code.dto.Response;
import com.robot.code.dto.TenantRobotDTO;

/**
 * @Author mrt
 * @Date 2020/5/25 17:08
 * @Version 2.0
 */
public interface IManagerFacde {
    /**
     * 增加机器人
     * @param robotDTO
     * @return
     */
    public Response addRobot(TenantRobotDTO robotDTO);

    /**
     * 删除机器人
     * @param robotId
     * @return
     */
    public Response deleteRobot(long robotId);

    /**
     * 更新机器人
     * @param robotDTO
     * @return
     */
    public Response updateRobot(TenantRobotDTO robotDTO);

    /**
     * 页面展示机器人
     * @param robotDTO
     * @return
     */
    public Response pageRobot(TenantRobotDTO robotDTO);

    /**
     * 通过id获取机器人
     * @param robotId
     * @return
     */
    public Response getRobotById(long robotId);

    /**
     * 上线机器人
     * @param robotWrapper
     * @return
     */
    public boolean online(RobotWrapper robotWrapper);

    /**
     * 下线机器人
     * @param robotId
     * @return
     */
    public boolean offline(long robotId);

    /**
     * 指定获取Cookie
     * @param robotId
     * @return
     */
    public RobotWrapper getCookie(long robotId);

    /**
     * 从队列轮询Cookie
     * @return
     */
    public RobotWrapper popCookie();
}
