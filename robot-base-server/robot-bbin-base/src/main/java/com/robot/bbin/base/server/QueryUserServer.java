package com.robot.bbin.base.server;

import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.bbin.base.function.QueryBalanceFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 查询会员是否存在
 * @Author mrt
 * @Date 2020/6/15 15:12
 * @Version 2.0
 */
@Service
public class QueryUserServer implements IAssemFunction<String> {
    @Autowired
    private QueryBalanceFunction queryUserFunction;

    @Override
    public Response doFunction(ParamWrapper<String> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<QueryBalanceBO> response = queryUserFunction.doFunction(paramWrapper, robotWrapper);
        if (!response.isSuccess()) {
            return response;
        }
        return Response.SUCCESS("会员存在");
    }
}
