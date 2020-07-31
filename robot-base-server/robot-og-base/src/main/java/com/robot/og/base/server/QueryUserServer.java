package com.robot.og.base.server;

import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.QueryUserAO;
import com.robot.og.base.bo.QueryUserBO;
import com.robot.og.base.function.QueryUserFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author mrt
 * @Date 2020/6/15 15:12
 * @Version 2.0
 * 查询用户
 */
@Service
public class QueryUserServer implements IAssemFunction<String> {

    @Autowired
    private QueryUserFunction queryUserFunction;

    @Override
    public Response doFunction(ParamWrapper<String> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<QueryUserBO> queryUserBOResponse = queryUserFunction.doFunction(creatParams(paramWrapper), robotWrapper);
        return queryUserBOResponse;
    }
    /**
     * 查询用户参数组装
     */
    private ParamWrapper<QueryUserAO> creatParams(ParamWrapper<String> paramWrapper) {
        String userDto = paramWrapper.getObj();
        QueryUserAO ao = new QueryUserAO();
        ao.setType("queryManDeposit");
        ao.setAccount(userDto);


        return new ParamWrapper<QueryUserAO>(ao);
    }


}
