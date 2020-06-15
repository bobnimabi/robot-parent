package com.robot.gpk.activity.interceptor;

import com.robot.center.constant.RobotConsts;
import com.robot.center.tenant.TenantInterceptor;
import com.robot.core.common.TContext;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/6/2 14:01
 * @Version 2.0
 */
@Service
public class GpkActivityTenant extends TenantInterceptor {
    @Override
    protected void setDevVariable() {
        TContext.setTenantId("5");
        TContext.setChannelId("5");
    }

    @Override
    protected void setPlatFormIdAndFunction() {
        TContext.setPlatformId(RobotConsts.PLATFORM_ID.GPK);
        TContext.setFunction(RobotConsts.FUNCTION_CODE.ACTIVITY);
    }
}
