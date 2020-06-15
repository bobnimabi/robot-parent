package com.robot.bbin.activity.function;

import com.bbin.common.client.TenantBetDetailVo;
import com.bbin.common.client.TenantBetVo;
import com.bbin.common.dto.robot.BreakThroughDTO;
import com.robot.gpk.base.ao.BetAO;
import com.robot.gpk.base.ao.BetDetailAO;
import com.robot.bbin.base.bo.BetBO;
import com.robot.bbin.base.bo.BetDetailBO;
import com.robot.bbin.base.bo.InOutCashBO;
import com.robot.bbin.base.function.BetDetailServer;
import com.robot.bbin.base.function.BetServer;
import com.robot.code.dto.Response;
import com.robot.core.function.base.IFunction;
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
 * 获取平台投注总金额和总损益
 */
@Slf4j
@Service
public class BetAmountAndRechargeServer implements IFunction<BreakThroughDTO,Object, TenantBetDetailVo> {

    @Autowired
    private BetDetailServer betDetailServer;
    @Autowired
    private BetServer betServer;
    @Autowired
    private QueryRechargeServer queryRecharge;

    @Override
    public Response doFunction(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        BreakThroughDTO breakThroughDTO = paramWrapper.getObj();
        TenantBetDetailVo betAndLoss = new TenantBetDetailVo();
        betAndLoss.setTotalBet(BigDecimal.ZERO);
        betAndLoss.setIncome(BigDecimal.ZERO);
        // 查询总投注
        Response<List<BetBO>> betResult = betServer.doFunction(new ParamWrapper<BetAO>(betAO(breakThroughDTO)), robotWrapper);
        if (!betResult.isSuccess()) {
            return betResult;
        }
        List<BetBO> betBOs = betResult.getObj();

        if (!CollectionUtils.isEmpty(betBOs)) {
            // 查询投注细节
            Response<List<BetDetailBO>> betDetailResult = betDetailServer.doFunction(new ParamWrapper<BetDetailAO>(detailParams(breakThroughDTO, betBOs.get(0).getKey())), robotWrapper);
            if (!betDetailResult.isSuccess()) {
                return betDetailResult;
            }
            this.statistic(betDetailResult,betAndLoss,breakThroughDTO);
        }

        // 查询充值金额
        Response<InOutCashBO> inOutCashBOResponse = queryRecharge.doFunction(paramWrapper, robotWrapper);
        InOutCashBO inOutCashBO = inOutCashBOResponse.getObj();
        if (!CollectionUtils.isEmpty(inOutCashBO.getList())) {
            betAndLoss.setIncome(inOutCashBO.getList().get(0).getDep_amount());
        }
        return Response.SUCCESS(betAndLoss);
    }

    private void statistic(Response<List<BetDetailBO>> betDetailResult, TenantBetDetailVo betAndLoss, BreakThroughDTO breakThroughDTO) {
        List<BetDetailBO> list = betDetailResult.getObj();
        List<TenantBetVo> tenantBets = betAndLoss.getTenantBets();
        HashSet<String> platCodeSet = new HashSet<String>(breakThroughDTO.getGameCodeList().size());
        for (String platCode : breakThroughDTO.getGameCodeList()) {
            platCodeSet.add(platCode);
        }
        for (BetDetailBO betDetailBO : list) {
            TenantBetVo tenantBetVo = new TenantBetVo();
            tenantBetVo.setGameCode(betDetailBO.getGameCode());
            tenantBetVo.setBetAmount(betDetailBO.getCommissionable());
            tenantBets.add(tenantBetVo);
            boolean contains = platCodeSet.contains(betDetailBO.getGameCode());
            if (contains) {
                betAndLoss.setTotalBet(betAndLoss.getTotalBet().add(betDetailBO.getCommissionable()));
            }
        }
    }

    /**
     * 投注总金额参数
     */
    private BetAO betAO(BreakThroughDTO breakThroughDTO)throws Exception {
        BetAO betAO = new BetAO();
        betAO.setStart(getDate(breakThroughDTO.getBeginDate()));
        betAO.setEnd(getDate(breakThroughDTO.getEndDate()));
        betAO.setName(breakThroughDTO.getUserName());
        return betAO;
    }

    /**
     * 投注细节参数
     */
    private BetDetailAO detailParams(BreakThroughDTO breakThroughDTO, String listid)throws Exception {
        BetDetailAO betDetailDTO = new BetDetailAO();
        betDetailDTO.setStart(getDate(breakThroughDTO.getBeginDate()));
        betDetailDTO.setEnd(getDate(breakThroughDTO.getEndDate()));
        betDetailDTO.setListid(listid);
        return betDetailDTO;
    }
    private String getDate(String dateTime) {
        return dateTime.substring(0, dateTime.indexOf(" "));
    }
}
