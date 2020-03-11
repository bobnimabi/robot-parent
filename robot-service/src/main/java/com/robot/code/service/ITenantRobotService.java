package com.robot.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbin.common.response.ResponseResult;

import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobot;

/**
 * <p>
 * 机器人表 服务类
 * </p>
 * @author admin
 * @since 2019-10-21
 */
public interface ITenantRobotService extends IService<TenantRobot> {
    // ---------------管理界面接口---------------
    /**
     * 机器人：增加
     */
    ResponseResult addRobot(LoginDTO robot);

    /**
     * 机器人：删除
     */
    ResponseResult deleteRobot(long robotId);

    /**
     * 机器人：修改
     */
    ResponseResult updateRobot(LoginDTO robot);

    /**
     * 机器人：分页查询
     */
    ResponseResult pageRobot(LoginDTO robotDTO);

    /**
     * 机器人：根据id查询
     */
    ResponseResult getRobotById(long robotId);

    /**
     * 机器人：强制关闭和下线
     */
    ResponseResult closeRobot(long robotId);


}
