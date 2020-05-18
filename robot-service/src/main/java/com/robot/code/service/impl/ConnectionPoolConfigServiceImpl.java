package com.robot.code.service.impl;

import com.robot.code.entity.ConnectionPoolConfig;
import com.robot.code.entity.TenantRobotConfig;
import com.robot.code.mapper.ConnectionPoolConfigMapper;
import com.robot.code.service.IConnectionPoolConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.robot.code.service.ITenantRobotConfigService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * httpclient的连接池配置 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Service
public class ConnectionPoolConfigServiceImpl extends ServiceImpl<ConnectionPoolConfigMapper, ConnectionPoolConfig> implements IConnectionPoolConfigService {

    @Autowired
    private ITenantRobotConfigService configService;

    @Override
    public ConnectionPoolConfig getPoolConfig() {
        TenantRobotConfig robotConfig = configService.getRobotConfig();
        Long poolId = robotConfig.getPoolId();
        ConnectionPoolConfig poolConfig = getById(poolId);
        if (null == poolConfig) {
            throw new IllegalArgumentException("PoolConfig未配置：id：" + poolId);
        }
        return null;
    }
}
