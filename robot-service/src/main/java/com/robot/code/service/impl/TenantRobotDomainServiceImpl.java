package com.robot.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.robot.code.entity.TenantRobotDomain;
import com.robot.code.mapper.TenantRobotDomainMapper;
import com.robot.code.service.ITenantRobotDomainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 域名表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Service
public class TenantRobotDomainServiceImpl extends ServiceImpl<TenantRobotDomainMapper, TenantRobotDomain> implements ITenantRobotDomainService {

    public TenantRobotDomain getDomain(int rank) {
        TenantRobotDomain one = getOne(new LambdaQueryWrapper<TenantRobotDomain>().eq(TenantRobotDomain::getRank, rank));
        if (null == one) {
            throw new IllegalStateException("未获取到Domain");
        }
        return one;
    }



}
