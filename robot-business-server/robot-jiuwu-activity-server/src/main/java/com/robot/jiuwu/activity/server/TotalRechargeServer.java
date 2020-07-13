package com.robot.jiuwu.activity.server;

import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.bbin.common.vo.VipTotalAmountVO;
import com.robot.center.util.MoneyUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.ao.TotalRechargeAO;
import com.robot.jiuwu.base.function.TotalRechargeDetailFunction;
import com.robot.jiuwu.base.vo.RechargeData;
import com.robot.jiuwu.base.vo.RechargeResultBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 所有游戏打码总量之和
 */
@Slf4j
@Service
public class TotalRechargeServer implements IAssemFunction<VipTotalAmountDTO> {

    @Autowired
    private TotalRechargeDetailFunction rechargeDetailFunction;


    @Override
    public Response doFunction(ParamWrapper<VipTotalAmountDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

        Response <RechargeResultBO>  rechargeDetailResult = rechargeDetailFunction.doFunction(getTotalRechargeAO(paramWrapper), robotWrapper);
        if (!rechargeDetailResult.isSuccess()) {
            return rechargeDetailResult;
        }

        RechargeResultBO rechargeResultVO =  rechargeDetailResult.getObj();

        List<RechargeData> rechargeDataList = rechargeResultVO.getData();

        // 累加打码金额
        VipTotalAmountVO vipTotalAmountVO = new VipTotalAmountVO();
        vipTotalAmountVO.setTotalAmount(BigDecimal.ZERO);
        if (!CollectionUtils.isEmpty(rechargeDataList)) {
            rechargeDataList.forEach(o->{
                vipTotalAmountVO.setTotalAmount(vipTotalAmountVO.getTotalAmount().add(o.getGrade()));
            });
        }
        // 分转元
        vipTotalAmountVO.setTotalAmount(MoneyUtil.convertToYuan(vipTotalAmountVO.getTotalAmount()));
        return Response.SUCCESS(vipTotalAmountVO);
    }

    /**
     * 查询游戏打码总量之和参数组装
     */
    private ParamWrapper<TotalRechargeAO> getTotalRechargeAO(ParamWrapper<VipTotalAmountDTO> paramWrapper) {
        VipTotalAmountDTO queryDTO = paramWrapper.getObj();
        TotalRechargeAO ao = new TotalRechargeAO();
        ao.setGameid(queryDTO.getUserName());
        ao.setStart(queryDTO.getBeginDate());
        ao.setEnd(queryDTO.getEndDate());

        return new ParamWrapper<TotalRechargeAO>(ao);
    }
}
