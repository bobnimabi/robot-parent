package com.robot.jiuwu.activity.function;

import com.bbin.common.client.TenantBetDetailVo;
import com.bbin.common.client.TenantBetVo;
import com.bbin.common.dto.robot.BreakThroughDTO;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.util.MoneyUtil;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.dto.OfflineDataDTO;
import com.robot.jiuwu.base.dto.OnlineRechargeDTO;
import com.robot.jiuwu.base.dto.TotalRechargeDTO;
import com.robot.jiuwu.base.function.OfflineRechargeServer;
import com.robot.jiuwu.base.function.OnlineRechargeServer;
import com.robot.jiuwu.base.function.TotalRechargeDetailServer;
import com.robot.jiuwu.base.vo.*;
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
    private OfflineRechargeServer offlineRechargeServer;
    @Autowired
    private OnlineRechargeServer onlineRechargeServer;
    @Autowired
    private TotalRechargeDetailServer rechargeDetailServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        BreakThroughDTO breakThroughDTO = paramWrapper.getObj();

        // 查询总投注和总损益
        ResponseResult rechargeDetailResult = rechargeDetailServer.doFunction(new ParamWrapper<TotalRechargeDTO>(createTotalRechargeDTO(breakThroughDTO)), robotWrapper);
        if (!rechargeDetailResult.isSuccess()) {
            return rechargeDetailResult;
        }

        RechargeResultVO rechargeResultVO = (RechargeResultVO) rechargeDetailResult.getObj();
        List<RechargeData> rechargeDataList = rechargeResultVO.getData();
        TenantBetDetailVo betAndLoss = createBetAndLoss(rechargeDataList);


        //
        ResponseResult offlineResult = offlineRechargeServer.doFunction(new ParamWrapper<OfflineDataDTO>(createOfflineParams(breakThroughDTO)), robotWrapper);
        if (!offlineResult.isSuccess()) {
            return rechargeDetailResult;
        }
        OfflineRechargeVO offlineRechargeVO = (OfflineRechargeVO) offlineResult.getObj();
        OfflineRechargeData data = offlineRechargeVO.getData();
        data.setAmount(MoneyUtil.convertToYuan(data.getAmount()));


        ResponseResult onlineResult = onlineRechargeServer.doFunction(new ParamWrapper<OnlineRechargeDTO>(createOnlineParams(breakThroughDTO)), robotWrapper);
        if (!onlineResult.isSuccess()) {
            return rechargeDetailResult;
        }
        OnlineRechargeVO onlineRechargeVO = (OnlineRechargeVO) onlineResult.getObj();

        betAndLoss.setIncome(offlineRechargeVO.getData().getAmount().add(onlineRechargeVO.getData().getAmount()));
//        betAndLoss.getTenantChannelUser().setUserName(breakThroughDTO.getUserName());
        return ResponseResult.SUCCESS(betAndLoss);
    }

    @Override
    public IActionEnum getActionEnum() {
        return null;
    }

    /**
     * 区间查询参数
     */
    private TotalRechargeDTO createTotalRechargeDTO(BreakThroughDTO breakThroughDTO)throws Exception {
        TotalRechargeDTO rechargeDTO = new TotalRechargeDTO(breakThroughDTO.getUserName(), breakThroughDTO.getBeginDate(), breakThroughDTO.getEndDate());

        return rechargeDTO;
    }

    /**
     * 创建线下充值参数
     */
    private OfflineDataDTO createOfflineParams(BreakThroughDTO breakThroughDTO) throws Exception {
        OfflineDataDTO offlineDataDTO = new OfflineDataDTO();
        offlineDataDTO.setTypes(new int[]{0});
        offlineDataDTO.setOrderdatebegin(breakThroughDTO.getBeginDate());
        offlineDataDTO.setOrderdateend(breakThroughDTO.getEndDate());
        offlineDataDTO.setCurrent(1);
        offlineDataDTO.setGameid(breakThroughDTO.getUserName());
        offlineDataDTO.setSize(20);
        return offlineDataDTO;
    }

    /**
     * 创建线上充值参数
     */
    private OnlineRechargeDTO createOnlineParams(BreakThroughDTO breakThroughDTO) throws Exception {
        OnlineRechargeDTO onlineDataDTO = new OnlineRechargeDTO();
        onlineDataDTO.setOrderstatus("1");
        onlineDataDTO.setPaydate_start(breakThroughDTO.getBeginDate());
        onlineDataDTO.setPaydate_end(breakThroughDTO.getEndDate());
        onlineDataDTO.setCurrent(1);
        onlineDataDTO.setSize(20);
        onlineDataDTO.setGameid(breakThroughDTO.getUserName());
        return onlineDataDTO;
    }
    /**
     * 填充总投注和总损益
     */
    private TenantBetDetailVo createBetAndLoss(List<RechargeData> rechargeDataList) {
        TenantBetDetailVo vo = new TenantBetDetailVo();
        BigDecimal totalBet = BigDecimal.ZERO;
        BigDecimal totalShou = BigDecimal.ZERO;
        List<TenantBetVo> list = vo.getTenantBets();
        for (RechargeData rechargeData : rechargeDataList) {
            totalBet = totalBet.add(rechargeData.getGrade());//投注，单位：分
            totalShou = totalShou.add(rechargeData.getScore());// 损益，单位：分
            //详细暂时不返回
            TenantBetVo tenantBetVo = new TenantBetVo();
            tenantBetVo.setBetAmount(MoneyUtil.convertToYuan(rechargeData.getGrade()));
            tenantBetVo.setLoseAmount(MoneyUtil.convertToYuan(rechargeData.getScore()));
//            tenantBetVo.setGameId((long)rechargeData.getKindID());// 临时去掉，那边不用，防止有问题
            list.add(tenantBetVo);
        }
        vo.setTotalBet(MoneyUtil.convertToYuan(totalBet));
        vo.setTotalLoss(MoneyUtil.convertToYuan(totalShou));
        return vo;
    }
}
