package com.robot.code.service;

import com.robot.code.entity.TenantRobotProxy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 请求代理
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface ITenantRobotProxyService extends IService<TenantRobotProxy> {

    TenantRobotProxy getProxy(long robotId);

}
