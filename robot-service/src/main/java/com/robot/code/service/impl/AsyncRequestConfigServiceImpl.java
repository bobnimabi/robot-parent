package com.robot.code.service.impl;

import com.robot.code.entity.AsyncRequestConfig;
import com.robot.code.entity.TenantRobotPath;
import com.robot.code.mapper.AsyncRequestConfigMapper;
import com.robot.code.service.IAsyncRequestConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.robot.code.service.ITenantRobotPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-29
 */
@Service
public class AsyncRequestConfigServiceImpl extends ServiceImpl<AsyncRequestConfigMapper, AsyncRequestConfig> implements IAsyncRequestConfigService {
    @Autowired
    private ITenantRobotPathService pathService;

    @Override
    public AsyncRequestConfig get(String pathCode) {
        TenantRobotPath path = pathService.getPath(pathCode);
        if (null != path.getAsyncRequestConfigId()) {
            AsyncRequestConfig config = getById(path.getAsyncRequestConfigId());
            if (null == config) {
                throw new IllegalArgumentException("未获取到AsyncRequestConfig,pathCode:" + pathCode + "AsyncRequestConfigId:" + path.getId());
            }
            return config;
        }
        return null;
    }
}
