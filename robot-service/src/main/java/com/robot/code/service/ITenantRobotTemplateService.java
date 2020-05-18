package com.robot.code.service;

import com.robot.code.entity.TenantRobotTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 显示栏目模板表 服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface ITenantRobotTemplateService extends IService<TenantRobotTemplate> {
    /**
     * 获取登录末班
     * @return
     */
    List<TenantRobotTemplate> getTemplate();
}
