package com.robot.bbin.activity.server;

import com.bbin.common.client.TenantBetDetailVo;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.BetAO2;
import com.robot.bbin.base.bo.BetBO;
import com.robot.bbin.base.bo.InOutCashBO;
import com.robot.bbin.base.function.BetFunction2;

import com.robot.center.util.DateUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 使用：获取平台投注总金额和总损益
 */
@Slf4j
@Service
public class BetAmountRechargeLossServer implements IAssemFunction<OrderNoQueryDTO> {
    @Autowired
    private BetFunction2 betServer;

    @Autowired
    private RechargeServer queryRecharge;

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        OrderNoQueryDTO breakThroughDTO = paramWrapper.getObj();
        TenantBetDetailVo betAndLoss = new TenantBetDetailVo();
        betAndLoss.setTotalBet(BigDecimal.ZERO);
        betAndLoss.setIncome(BigDecimal.ZERO);
        // 查询总投注
        Response<List<BetBO> > betResult = betServer.doFunction(new ParamWrapper<BetAO2>(betAO(breakThroughDTO)), robotWrapper);
        if (!betResult.isSuccess()) {
            return betResult;
        }
        List<BetBO>  betBO = betResult.getObj();

            betAndLoss.setTotalBet(betBO.get(0).getCommissionable());
            betAndLoss.setTotalLoss(betBO.get(0).getPayoff());


        // 查询充值金额
        Response<InOutCashBO> inOutCashBOResponse = queryRecharge.doFunction(paramWrapper, robotWrapper);
        InOutCashBO inOutCashBO = inOutCashBOResponse.getObj();
        if (null==inOutCashBO) {
            return Response.FAIL("未查询到充值金额");
        }else {
            betAndLoss.setIncome(inOutCashBO.getList().get(0).getDep_amount());
        }

        return Response.SUCCESS(betAndLoss);
    }
    /**
     * 投注总金额参数
     */
    private BetAO2 betAO(OrderNoQueryDTO dto)throws Exception {
        BetAO2 betAO = new BetAO2();
        betAO.setStart(DateUtils.getYyyyMMdd(dto.getStartDate()));
        betAO.setEnd(DateUtils.getYyyyMMdd(dto.getEndDate()));
        betAO.setName(dto.getUserName());
        betAO.setGame(dto.getGameCode());
        return betAO;
    }


    private String getDate(String dateTime) {
        return dateTime.substring(0, dateTime.indexOf(" "));
    }

}
