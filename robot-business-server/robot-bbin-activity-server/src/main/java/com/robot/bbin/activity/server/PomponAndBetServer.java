package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.GameChild;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.vo.OrderNoQueryVO;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.bbin.base.bo.JuQueryDetailBO;
import com.robot.bbin.base.bo.TotalBetGameBO;
import com.robot.center.util.MoneyUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import javafx.scene.effect.SepiaTone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 彩球加赠
 * 查询彩球个数+平均下单金额
 * @Author mrt
 * @Date 2020/6/2 16:34
 * @Version 2.0
 */
@Service
public class PomponAndBetServer implements IAssemFunction<OrderNoQueryDTO> {
    @Autowired
    private PomponServer pomponServer;

    @Autowired
    private GameBetServer gameBetServer;

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        OrderNoQueryDTO orderNoQueryDTO = paramWrapper.getObj();
        OrderNoQueryVO breakerQueryVO = new OrderNoQueryVO();
        breakerQueryVO.setBallNumber(0);

        // 查询总的彩球个数，同时对会员账号，无彩球，日期，游戏 进行校验
        for (String orderNo : orderNoQueryDTO.getOrderNos()) {
            Response<JuQueryDetailBO> breakResult = pomponServer.doFunction(pomponParams(orderNoQueryDTO, orderNo), robotWrapper);
            if (!breakResult.isSuccess()) {
                return breakResult;
            }
            JuQueryDetailBO detailBO = breakResult.getObj();
            breakerQueryVO.setBallNumber(breakerQueryVO.getBallNumber() + detailBO.getBallNumber());
        }

        Response<List<TotalBetGameBO>> betListResult = gameBetServer.doFunction(paramWrapper, robotWrapper);

        if (!betListResult.isSuccess()) {
            return betListResult;
        }
        List<TotalBetGameBO> list = betListResult.getObj();
        BigDecimal average = calcAverage(list, orderNoQueryDTO);

        breakerQueryVO.setAverageAmount(average);
        return Response.SUCCESS(breakerQueryVO);
    }

    private ParamWrapper<OrderNoQueryDTO> pomponParams(OrderNoQueryDTO orderNoQueryDTO,String orderNo) {
        OrderNoQueryDTO orderBat = MyBeanUtil.copyProperties(orderNoQueryDTO, OrderNoQueryDTO.class);
        orderBat.setOrderNo(orderNo);
        return new ParamWrapper<OrderNoQueryDTO>(orderBat);
    }

    /**
     * 计算所有注单平均下单金额
     */
    private BigDecimal calcAverage(List<TotalBetGameBO> list,  OrderNoQueryDTO obj) {
        Set<String> set = new HashSet<String>(8);
        for (GameChild child : obj.getChildren()) {
            set.add(child.getName());
        }
        BigDecimal totalBet = BigDecimal.ZERO;
        int count = 0;
        for (TotalBetGameBO totalBetGameVO : list) {
            if (set.contains(totalBetGameVO.getGameName())) {
                count += totalBetGameVO.getNum();
                totalBet = totalBet.add(totalBetGameVO.getTotalBetByGame());
            }
        }
        return totalBet.divide(new BigDecimal(count),BigDecimal.ROUND_DOWN);
    }

}
