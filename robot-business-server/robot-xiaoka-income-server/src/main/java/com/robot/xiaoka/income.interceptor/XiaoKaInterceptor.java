package com.robot.xiaoka.income.interceptor;

import com.robot.center.constant.RobotConsts;
import com.robot.center.interceptor.TenantInterceptor;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/3/9 17:14
 */
@Service
public class XiaoKaInterceptor extends TenantInterceptor {

    @Override
    protected Long getPlatFormId() {
        return RobotConsts.PLATFORM_ID.REN_REN_XIAO_KA;
    }

    @Override
    protected Long getFunctionCode() {
        return RobotConsts.FUNCTION_CODE.INCOME;
    }
}
