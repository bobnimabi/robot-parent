package com.robot.bbin.activity.server;

import com.bbin.common.client.TenantBetDetailVo;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.BetAO2;
import com.robot.bbin.base.ao.RebateAO;
import com.robot.bbin.base.bo.BetBO;
import com.robot.bbin.base.bo.InOutCashBO;
import com.robot.bbin.base.function.BetFunction2;
import com.robot.bbin.base.function.RebateFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tanke on 11/14/2019 8:06 PM
 * 使用：获取平台投注总金额和总损益
 */
@Slf4j
@Service
public class BetAmountRechargeLossServer implements IAssemFunction<OrderNoQueryDTO> {
    @Autowired
    private BetFunction2 betServer;
    @Autowired
    private RebateFunction rebateFunction;

    @Autowired
    private RechargeServer queryRecharge;

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();
        TenantBetDetailVo betAndLoss = new TenantBetDetailVo();
        betAndLoss.setTotalBet(BigDecimal.ZERO);
        betAndLoss.setIncome(BigDecimal.ZERO);
        // 查询总投注
        Response<List<BetBO> > betResult = betServer.doFunction(new ParamWrapper<BetAO2>(betAO(queryDTO)), robotWrapper);
        if (!betResult.isSuccess()) {
            return betResult;
        }
        List<BetBO>  betBO = betResult.getObj();

            betAndLoss.setTotalBet(betBO.get(0).getCommissionable());

            //派彩金额为负数时候，为亏损金额，需要加上优惠活动金额
        Response<String> rebateBeanResponse = rebateFunction.doFunction(creatRebatAOparams(queryDTO),robotWrapper);
        String amount = rebateBeanResponse.getObj();
        betAndLoss.setTotalLoss(betBO.get(0).getPayoff().add(new BigDecimal(amount)));

        // 查询充值金额
        Response<InOutCashBO> inOutCashBOResponse = queryRecharge.doFunction(paramWrapper, robotWrapper);
        InOutCashBO inOutCashBO = inOutCashBOResponse.getObj();
        if (null==inOutCashBO) {
            return Response.FAIL("未查询到充值金额");
        }else if(inOutCashBO.getList().get(0).getDep_amount().compareTo(BigDecimal.valueOf(100)) < 0){
            return Response.FAIL("7天内充值金额小于100");
        } else {
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

    /**
     *  查询优惠参数组装
     */
    private ParamWrapper<RebateAO>  creatRebatAOparams(OrderNoQueryDTO dto){
        RebateAO rebateAO = new RebateAO();
        rebateAO.setStart(DateUtils.getYyyyMMdd(dto.getStartDate()));
        rebateAO.setEnd(DateUtils.getYyyyMMdd(dto.getEndDate()));
        rebateAO.setName(dto.getUserName());
        return new ParamWrapper<>(rebateAO);
    }

}
