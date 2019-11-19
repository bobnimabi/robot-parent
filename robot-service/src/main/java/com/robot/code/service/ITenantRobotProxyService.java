package com.robot.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.robot.code.entity.TenantRobotProxy;

/**
 * <p>
 * 机器人表 服务类
 * </p>
 *
 * @author admin
 * @since 2019-10-21
 */
public interface ITenantRobotProxyService extends IService<TenantRobotProxy> {

    /**
     * 查看代理
     * @param robotId
     * @return
     */
    TenantRobotProxy getByRobotId(long robotId);


}
