package com.robot.gpk.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.vo.OrderNoQueryVO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.bo.JuQueryDetailBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * 使用：查询消消除游戏
 * @Author mrt
 * @Date 2020/6/2 16:34
 * @Version 2.0
 */
@Service
public class BreakAndBetServer implements IAssemFunction<OrderNoQueryDTO> {

    @Autowired
    private BreakServer breakServer;

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<JuQueryDetailBO> breakResult = breakServer.doFunction(paramWrapper, robotWrapper);
        if (!breakResult.isSuccess()) {
            return breakResult;
        }
        JuQueryDetailBO detailBO = breakResult.getObj();
        OrderNoQueryVO breakerQueryVO = new OrderNoQueryVO();
        breakerQueryVO.setAccumulativeWins(detailBO.getLevel());
        breakerQueryVO.setGameName(detailBO.getGameName());
        breakerQueryVO.setOrderTime(detailBO.getOrderTime());
        breakerQueryVO.setPlatFormOrderNo(detailBO.getPlatFormOrderNo());
        breakerQueryVO.setUserName(detailBO.getUserName());
        breakerQueryVO.setGameCode(detailBO.getGameCode());
        breakerQueryVO.setRebateAmount(detailBO.getRebateAmount());
        BigDecimal rebateAmount = detailBO.getRebateAmount();

        //BigDecimal转换成int类型做判断  金额大于2元才能申请
        BigDecimal a=new BigDecimal(String.valueOf(rebateAmount));
        int b=a.intValue();
        System.out.println(b);

        if(b <2){
            return Response.FAIL("单注下注金额小于2元无法申请");
        }
        return Response.SUCCESS(breakerQueryVO);
    }


}
