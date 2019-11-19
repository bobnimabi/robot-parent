package com.robot.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.robot.code.entity.TenantRobotTemplate;

import java.util.List;

/**
 * <p>
 * 用户参与某个活动的申请填写数据模板明细 服务类
 * </p>
 * @author admin
 * @since 2019-10-21
 */
public interface ITenantRobotTemplateService extends IService<TenantRobotTemplate> {

    List<TenantRobotTemplate> getTemplate();

}