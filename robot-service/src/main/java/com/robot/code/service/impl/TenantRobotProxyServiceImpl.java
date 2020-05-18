package com.robot.code.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.robot.code.entity.TenantRobotProxy;
import com.robot.code.mapper.TenantRobotProxyMapper;
import com.robot.code.service.ITenantRobotProxyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 机器人表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2019-10-21
 */
@Service
public class TenantRobotProxyServiceImpl extends ServiceImpl<TenantRobotProxyMapper, TenantRobotProxy> implements ITenantRobotProxyService {

    @Override
    public TenantRobotProxy getProxy(long robotId) {
        TenantRobotProxy proxy = getOne(
                new LambdaQueryWrapper<TenantRobotProxy>()
                .eq(TenantRobotProxy::getRobotId, robotId)
        );
        return proxy;
    }
}
