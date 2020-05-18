package com.robot.code.service;

import com.robot.code.entity.TenantRobotDomain;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 域名表 服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface ITenantRobotDomainService extends IService<TenantRobotDomain> {

    /**
     * 获取域名
     * @param rank
     * @return
     */
    TenantRobotDomain getDomain(int rank);

}
