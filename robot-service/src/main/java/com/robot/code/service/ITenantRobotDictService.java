package com.robot.code.service;

import com.robot.code.entity.TenantRobotDict;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 站点配置项 服务类
 * </p>
 *
 * @author admin
 * @since 2019-11-15
 */
public interface ITenantRobotDictService extends IService<TenantRobotDict> {

    public String getValue(String key);

}
