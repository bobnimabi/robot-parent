package com.robot.jiuwu.activity.server;


import com.bbin.common.client.TenantBetDetailVo;
import com.bbin.common.client.TenantBetVo;
import com.bbin.common.dto.robot.BreakThroughDTO;

import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.robot.center.util.MoneyUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.ao.TotalRechargeAO;
import com.robot.jiuwu.base.dto.OfflineDataDTO;
import com.robot.jiuwu.base.dto.OnlineRechargeDTO;
import com.robot.jiuwu.base.dto.TotalRechargeDTO;
import com.robot.jiuwu.base.function.OfflineRechargeFunction;
import com.robot.jiuwu.base.function.OnllineRechargeFunction;
import com.robot.jiuwu.base.function.TotalRechargeDetailFunction;
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
public class BetAmountAndRechargeServer implements IAssemFunction<BreakThroughDTO> {

    @Autowired
    private OfflineRechargeFunction offlineRechargeServer;
   @Autowired
    private OnllineRechargeFunction onlineRechargeServer;
    @Autowired
    private TotalRechargeDetailFunction rechargeDetailServer;


    @Override
    public Response doFunction(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {


        BreakThroughDTO breakThroughDTO = paramWrapper.getObj();

        // 查询总投注和总损益
        Response<RechargeResultBO> rechargeDetailResult = rechargeDetailServer.doFunction(getTotalRechargeAO(paramWrapper), robotWrapper);
        if (!rechargeDetailResult.isSuccess()) {
            return rechargeDetailResult;
        }

        RechargeResultBO rechargeResultVO = rechargeDetailResult.getObj();
        List<RechargeData> rechargeDataList = rechargeResultVO.getData();
        TenantBetDetailVo betAndLoss = createBetAndLoss(rechargeDataList);


        Response offlineResult = offlineRechargeServer.doFunction(new ParamWrapper<OfflineDataDTO>(createOfflineParams(breakThroughDTO)), robotWrapper);
        if (!offlineResult.isSuccess()) {
            return rechargeDetailResult;
        }
        OfflineRechargeVO offlineRechargeVO = (OfflineRechargeVO) offlineResult.getObj();
        OfflineRechargeData data = offlineRechargeVO.getData();

    //   data.setAmount(MoneyUtil.convertToYuan(data.getAmount()));

        Response onlineResult = onlineRechargeServer.doFunction(new ParamWrapper<OnlineRechargeDTO>(createOnlineParams(breakThroughDTO)), robotWrapper);
        if (!onlineResult.isSuccess()) {
            return rechargeDetailResult;
        }

        OnlineRechargeVO onlineRechargeVO = (OnlineRechargeVO) onlineResult.getObj();
        if(null==offlineRechargeVO.getData()&&null==onlineRechargeVO.getData()){
            return Response.FAIL("线上线下充值金额为0");
        }else if(offlineRechargeVO.getData()==null){
            betAndLoss.setIncome(onlineRechargeVO.getData().getAmount());
        }else if(onlineRechargeVO.getData()==null){
            betAndLoss.setIncome(MoneyUtil.convertToYuan(offlineRechargeVO.getData().getAmount()));//MoneyUtil.convertToYuan()
        }else {
            betAndLoss.setIncome(MoneyUtil.convertToYuan(offlineRechargeVO.getData().getAmount()).add(onlineRechargeVO.getData().getAmount()));
        }

//       betAndLoss.getTenantChannelUser().setUserName(breakThroughDTO.getUserName());
        return Response.SUCCESS(betAndLoss);
    }

/**
 * 区间查询参数
 */
   private ParamWrapper<TotalRechargeAO> getTotalRechargeAO(ParamWrapper<BreakThroughDTO> paramWrapper)throws Exception {

       BreakThroughDTO breakThroughDTO = paramWrapper.getObj();

       TotalRechargeAO rechargeDTO = new TotalRechargeAO();
       rechargeDTO.setGameid(breakThroughDTO.getUserName());
       rechargeDTO.setStart(breakThroughDTO.getBeginDate());
       rechargeDTO.setEnd(breakThroughDTO.getEndDate());



        return new ParamWrapper<TotalRechargeAO>(rechargeDTO);
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
