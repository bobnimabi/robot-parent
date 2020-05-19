package com.robot.core.function.base;

import com.bbin.common.response.ResponseResult;
import com.robot.code.entity.TenantRobotDomain;
import com.robot.core.robot.manager.RobotCard;

/**
 * Created by mrt on 11/15/2019 12:30 PM
 * 功能接口
 */
public interface IExccuteHelp<T> {

    /**
     * 获取域名等级
     * @return
     */
    int getRank();



}
