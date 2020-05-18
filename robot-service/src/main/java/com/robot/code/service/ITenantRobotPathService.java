package com.robot.code.service;

import com.robot.code.entity.TenantRobotPath;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 路径表 服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface ITenantRobotPathService extends IService<TenantRobotPath> {

    /**
     * 获取路径
     * @param actionCode
     * @param rank
     * @return
     */
    TenantRobotPath getPath(String actionCode, int rank);
}
