package com.robot.og.activity.interceptor;

import com.robot.center.constant.RobotConsts;
import com.robot.center.interceptor.TenantInterceptor;
import com.robot.core.common.TContext;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/6/2 14:01
 * @Version 2.0  //todo
 */
@Service
public class OGActivityTenant extends TenantInterceptor {
    @Override
    protected void setDevVariable() {
        TContext.setTenantId("7");
        TContext.setChannelId("7");
    }

    @Override
    protected void setPlatFormIdAndFunction() {
        TContext.setPlatformId(RobotConsts.PLATFORM_ID.OG);
        TContext.setFunction(RobotConsts.FUNCTION_CODE.ACTIVITY);
    }
}
