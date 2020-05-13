package com.robot.bbin.activity.function;

import com.bbin.common.client.TenantBetDetailVo;
import com.bbin.common.client.TenantBetVo;
import com.bbin.common.dto.robot.BreakThroughDTO;
import com.bbin.common.response.ResponseResult;
import com.robot.bbin.base.dto.BetDTO;
import com.robot.bbin.base.dto.BetDetailDTO;
import com.robot.bbin.base.dto.InOutCashDTO;
import com.robot.bbin.base.function.BetDetailServer;
import com.robot.bbin.base.function.BetServer;
import com.robot.bbin.base.function.InOutCashServer;
import com.robot.bbin.base.vo.BetData;
import com.robot.bbin.base.vo.BetDetailVO;
import com.robot.bbin.base.vo.BetVO;
import com.robot.bbin.base.vo.InOutCashVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 获取投注总金额和总损益
 */
@Slf4j
@Service
public class BetAmountAndRechargeServer extends FunctionBase<BreakThroughDTO> {
    @Autowired
    private BetDetailServer betDetailServer;
    @Autowired
    private BetServer betServer;
    @Autowired
    private InOutCashServer inOutCashServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        BreakThroughDTO breakThroughDTO = paramWrapper.getObj();
        TenantBetDetailVo betAndLoss = new TenantBetDetailVo();
        // 查询总投注和投注细节
        ResponseResult betResult = betServer.doFunction(new ParamWrapper<BetDTO>(createBetParams(breakThroughDTO)), robotWrapper);
        if (!betResult.isSuccess()) {
            return betResult;
        }

        BetVO betVO = (BetVO) betResult.getObj();
        BetData betData = betVO.getList().get(0);
        if (betVO.getList().size() == 0) {
            betAndLoss.setTotalBet(BigDecimal.ZERO);
        } else {
            betAndLoss.setTotalBet(betData.getCommissionable());
            ResponseResult betDetailResult = betDetailServer.doFunction(new ParamWrapper<BetDetailDTO>(createBetDetailParams(breakThroughDTO,betData.getKey())), robotWrapper);
            if (!betDetailResult.isSuccess()) {
                return betDetailResult;
            }
            List<BetDetailVO> list = (List<BetDetailVO>) betDetailResult.getObj();
            List<TenantBetVo> tenantBets = betAndLoss.getTenantBets();
            for (BetDetailVO betDetailVO : list) {
                TenantBetVo tenantBetVo = new TenantBetVo();
                tenantBetVo.setGameId(1L); //betDetailVO.getGameCode()
                tenantBetVo.setBetAmount(betDetailVO.getCommissionable());
                tenantBets.add(tenantBetVo);
            }
        }
        ResponseResult betDetailResult = inOutCashServer.doFunction(new ParamWrapper<InOutCashDTO>(createRechargeParams(breakThroughDTO)), robotWrapper);
        if (!betDetailResult.isSuccess()) {
            return betDetailResult;
        }
        InOutCashVO inOutCashVO = (InOutCashVO) betDetailResult.getObj();
        if (0 == inOutCashVO.getPage()) {
            betAndLoss.setIncome(BigDecimal.ZERO);
        } else {
            betAndLoss.setIncome(inOutCashVO.getList().get(0).getDep_amount());
        }
        return ResponseResult.SUCCESS(betAndLoss);
    }

    @Override
    public IActionEnum getActionEnum() {
        return null;
    }

    /**
     * 投注总金额参数
     */
    private BetDTO createBetParams(BreakThroughDTO breakThroughDTO)throws Exception {
        BetDTO betDTO = new BetDTO();
        betDTO.setStart(getDate(breakThroughDTO.getBeginDate()));
        betDTO.setEnd(getDate(breakThroughDTO.getEndDate()));
        betDTO.setName(breakThroughDTO.getUserName());
        return betDTO;
    }

    /**
     * 投注细节参数
     */
    private BetDetailDTO createBetDetailParams(BreakThroughDTO breakThroughDTO,String listid)throws Exception {
        BetDetailDTO betDetailDTO = new BetDetailDTO();
        betDetailDTO.setStart(getDate(breakThroughDTO.getBeginDate()));
        betDetailDTO.setEnd(getDate(breakThroughDTO.getEndDate()));
        betDetailDTO.setListid(listid);
        return betDetailDTO;
    }

    /**
     * 查询充值
     */
    private InOutCashDTO createRechargeParams(BreakThroughDTO breakThroughDTO)throws Exception {
        InOutCashDTO in = new InOutCashDTO();
        in.setStart(getDate(breakThroughDTO.getBeginDate()));
        in.setEnd(getDate(breakThroughDTO.getEndDate()));
        in.setMethed("0");
        in.setName(breakThroughDTO.getUserName());
        return in;
    }

    private String getDate(String dateTime) {
        return dateTime.substring(0, dateTime.indexOf(" "));
    }
}
