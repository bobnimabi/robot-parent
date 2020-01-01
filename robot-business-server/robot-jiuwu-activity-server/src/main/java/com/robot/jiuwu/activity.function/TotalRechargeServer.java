package com.robot.jiuwu.activity.function;

import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.bbin.common.response.ResponseResult;
import com.bbin.common.vo.VipTotalAmountVO;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.center.execute.IActionEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.util.MoneyUtil;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.dto.TotalRechargeDTO;
import com.robot.jiuwu.base.function.TotalRechargeDetailServer;
import com.robot.jiuwu.base.vo.RechargeData;
import com.robot.jiuwu.base.vo.RechargeResultVO;
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
public class TotalRechargeServer extends FunctionBase<VipTotalAmountDTO> {
    @Autowired
    private TotalRechargeDetailServer rechargeDetailServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<VipTotalAmountDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        VipTotalAmountDTO vipTotalAmountDTO = paramWrapper.getObj();
        TotalRechargeDTO rechargeDTO = MyBeanUtil.copyProperties(vipTotalAmountDTO, TotalRechargeDTO.class);

        ResponseResult rechargeDetailResult = rechargeDetailServer.doFunction(new ParamWrapper<TotalRechargeDTO>(rechargeDTO), robotWrapper);
        if (!rechargeDetailResult.isSuccess()) {
            return rechargeDetailResult;
        }

        RechargeResultVO rechargeResultVO = (RechargeResultVO) rechargeDetailResult.getObj();
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
        return ResponseResult.SUCCESS(vipTotalAmountVO);
    }

    @Override
    public IActionEnum getActionEnum() {
        //调度类，不需要具体的执行的Action
        return null;
    }
}
