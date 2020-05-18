package com.robot.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.robot.code.entity.TenantRobotHead;
import com.robot.code.mapper.TenantRobotHeadMapper;
import com.robot.code.service.ITenantRobotHeadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Service
public class TenantRobotHeadServiceImpl extends ServiceImpl<TenantRobotHeadMapper, TenantRobotHead> implements ITenantRobotHeadService {

    @Override
    public List<TenantRobotHead> getPublicHeaders() {
        return list();
    }
}
