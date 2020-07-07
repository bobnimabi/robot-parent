package com.robot.jiuwu.activity.interceptor;

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
public class JiuWuActivityTenant extends TenantInterceptor {
    @Override
    protected void setDevVariable() {
        TContext.setTenantId("7");
        TContext.setChannelId("7");
    }

    @Override
    protected void setPlatFormIdAndFunction() {
        TContext.setPlatformId(RobotConsts.PLATFORM_ID.JIU_WU_CARD);
        TContext.setFunction(RobotConsts.FUNCTION_CODE.ACTIVITY);
    }
}
