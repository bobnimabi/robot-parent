package com.robot.code.service.impl;

import com.robot.code.entity.ConnectionPoolConfig;
import com.robot.code.mapper.ConnectionPoolConfigMapper;
import com.robot.code.service.IConnectionPoolConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
