package com.robot.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.robot.code.entity.TenantRobotAction;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2019-10-21
 */
public interface ITenantRobotActionService extends IService<TenantRobotAction> {


    /**
     * 获取动作
     * @param actionCode 动作编码
     * @return
     */
    TenantRobotAction getAction(String actionCode);


}
