package com.robot.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.robot.code.entity.TenantRobotPath;
import com.robot.code.mapper.TenantRobotPathMapper;
import com.robot.code.service.ITenantRobotPathService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 路径表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Service
public class TenantRobotPathServiceImpl extends ServiceImpl<TenantRobotPathMapper, TenantRobotPath> implements ITenantRobotPathService {

    @Override
    public TenantRobotPath getPath(String pathCode) {
        TenantRobotPath one = getOne(new LambdaQueryWrapper<TenantRobotPath>()
                .eq(TenantRobotPath::getPathCode, pathCode));
        if (null == one) {
            throw new IllegalStateException("未获取到Path,pathCode:" + pathCode);
        }
        return one;
    }
}
