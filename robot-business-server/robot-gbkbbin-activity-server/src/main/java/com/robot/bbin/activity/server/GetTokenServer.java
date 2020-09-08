package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.robot.bbin.base.ao.TokenAO;
import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.bbin.base.function.QueryBalanceFunction;
import com.robot.bbin.base.function.XBBGetTokenFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 使用：获取token
 * @Author mrt
 * @Date 2020/6/2 14:56
 * @Version 2.0
 */
@Service
public class GetTokenServer implements IAssemFunction<OrderNoQueryDTO> {
    //查询用户id  用于获取token
    @Autowired
    private QueryBalanceFunction queryBalanceFunction;


    @Autowired
    private XBBGetTokenFunction tokenFunction;



    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {



        // 查询余额：查询UserID
        Response<QueryBalanceBO> balanceResult = queryBalanceFunction.doFunction(createQueryBalanceParams(paramWrapper), robotWrapper);
        if (!balanceResult.isSuccess()) {
            return balanceResult;
        }

        QueryBalanceBO balanceBO = balanceResult.getObj();
        // 局查询细节前置查询:获取Token
        Response<String> tokenResult = tokenFunction.doFunction(createTokenParams(paramWrapper,balanceBO), robotWrapper);
        return tokenResult;

    }

    /**
     * 组装查询userID参数    /
     * @param paramWrapper
     * @return
     */


    private ParamWrapper<String> createQueryBalanceParams(ParamWrapper<OrderNoQueryDTO> paramWrapper) {
        OrderNoQueryDTO breakThroughDTO = paramWrapper.getObj();
        return new ParamWrapper<String>(breakThroughDTO.getUserName());
    }




    /**
     *  查询token所用参数组装
     */
    private ParamWrapper<TokenAO> createTokenParams(ParamWrapper<OrderNoQueryDTO> paramWrapper, QueryBalanceBO balanceBO) {
        TokenAO tokendAO = new TokenAO();

        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        // 平台编码
        tokendAO.setGamekind(queryDTO.getGameCode());   //getGameCode  76
        // userId,或许可以通过查询余额那个接口获取(待验证)
        tokendAO.setUserid(balanceBO.getUser_id());
        // 注单号
        tokendAO.setWagersid(queryDTO.getOrderNo());

        return new ParamWrapper<>(tokendAO);
    }
}
