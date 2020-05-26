package com.robot.code.service;

import com.bbin.common.response.ResponseResult;
import com.robot.code.dto.Response;
import com.robot.code.dto.TenantRobotDTO;
import com.robot.code.entity.TenantRobot;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 机器人表 服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface ITenantRobotService extends IService<TenantRobot>  {
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
     * 上线机器人
     * @param robotId
     * @return
     */
    boolean onlineDB(long robotId);

    /**
     * 下线机器人
     * @param robotId
     * @return
     */
    boolean offlineDB(long robotId);
}
