package com.robot.code.service;

import com.robot.code.entity.TenantRobotHead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface ITenantRobotHeadService extends IService<TenantRobotHead> {

    /**
     * 获取公共请求头
     * @return
     */
    List<TenantRobotHead> getPublicHeaders();

}
