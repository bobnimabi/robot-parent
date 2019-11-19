package com.robot.code.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.robot.code.entity.TenantRobotTemplate;
import com.robot.code.mapper.TenantRobotTemplateMapper;
import com.robot.code.service.ITenantRobotTemplateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户参与某个活动的申请填写数据模板明细 服务实现类
 * </p>
 *
 * @author admin
 * @since 2019-10-21
 */
@Service
public class TenantRobotTemplateServiceImpl extends ServiceImpl<TenantRobotTemplateMapper, TenantRobotTemplate> implements ITenantRobotTemplateService {

    @Override
    public List<TenantRobotTemplate> getTemplate() {
        return list();
    }
}
