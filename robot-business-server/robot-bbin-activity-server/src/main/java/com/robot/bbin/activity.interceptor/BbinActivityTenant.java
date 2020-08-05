package com.robot.bbin.activity.interceptor;

import com.robot.center.constant.RobotConsts;
import com.robot.center.interceptor.TenantInterceptor;
import com.robot.core.common.TContext;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/6/2 14:01
 * @Version 2.0
 */
@Service
public class BbinActivityTenant extends TenantInterceptor {

    @Override
    protected void setDevVariable() {
        TContext.setTenantId("6");
        TContext.setChannelId("2");
    }

    @Override
    protected void setPlatFormIdAndFunction() {
        TContext.setPlatformId(RobotConsts.PLATFORM_ID.BBIN);
        TContext.setFunction(RobotConsts.FUNCTION_CODE.ACTIVITY);
    }
}
