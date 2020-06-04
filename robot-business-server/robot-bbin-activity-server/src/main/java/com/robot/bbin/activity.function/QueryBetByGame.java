package com.robot.bbin.activity.function;

import com.bbin.common.client.TenantBetDetailVo;
import com.bbin.common.dto.robot.BreakThroughDTO;
import com.robot.bbin.base.function.QueryBalanceServer;
import com.robot.bbin.base.function.TotalBetGame;
import com.robot.code.dto.Response;
import com.robot.core.function.base.IFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/6/3 12:05
 * @Version 2.0
 */
@Service
public class QueryBetByGame implements IFunction<BreakThroughDTO,Object, TenantBetDetailVo> {

    @Autowired
    private TotalBetGame totalBetGame;

    @Autowired
    private QueryBalanceServer queryBalanceServer;

    @Override
    public Response<TenantBetDetailVo> doFunction(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

        return null;
    }
}
