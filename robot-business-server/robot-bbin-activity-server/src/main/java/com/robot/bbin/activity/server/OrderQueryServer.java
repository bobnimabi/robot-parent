package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.util.DateUtils;
import com.robot.bbin.base.ao.BarIdAO;
import com.robot.bbin.base.function.BarIdFunction;
import com.robot.core.function.base.IAssemFunction;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.function.JuQueryFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 局查询（BB电子）
 * 使用：查询注单
 * @Author mrt
 * @Date 2020/6/2 16:34
 * @Version 2.0
 *
 */
@Service
public class OrderQueryServer implements IAssemFunction<OrderNoQueryDTO> {

    @Autowired
    private BarIdFunction barIdFunction;

    @Autowired
    private JuQueryFunction juQueryServer;

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();
        Response<String> barIdResult = barIdFunction.doFunction(barIDParams(queryDTO), robotWrapper);
        if (!barIdResult.isSuccess()) {
            return barIdResult;
        }
        Response<JuQueryBO> response = juQueryServer.doFunction(juQueryAO(queryDTO, barIdResult.getObj()), robotWrapper);
        if (!response.isSuccess()) {
            return response;
        }
        JuQueryBO juQueryBO = response.getObj();
        if (StringUtils.isEmpty(juQueryBO.getPageId()) || juQueryBO.getOrderTime().isBefore(queryDTO.getStartDate())) {
            return Response.FAIL("订单已过期,订单号："+juQueryBO.getPlatFormOrderNo());
        }
        if (!juQueryBO.getUserName().equals(queryDTO.getUserName())) {
            return Response.FAIL("会员账号不匹配，传入：" + queryDTO.getUserName() + " 实际：" + juQueryBO.getUserName());
        }
        return response;
    }
    /**
     *  BarID参数组装
     */
    private ParamWrapper<BarIdAO> barIDParams(OrderNoQueryDTO queryDTO) {
        BarIdAO barIdAO = new BarIdAO();
        barIdAO.setGameKind(queryDTO.getGameCode());
        barIdAO.setSearchData("BetQuery");
        barIdAO.setDate_start(LocalDateTime.now().format(DateUtils.DF_3));
        barIdAO.setDate_end(LocalDateTime.now().format(DateUtils.DF_3));
        return new ParamWrapper<BarIdAO>(barIdAO);
    }

    /**
     * 局查询参数组装
     */
    private ParamWrapper<JuQueryAO> juQueryAO(OrderNoQueryDTO queryDTO,String barID) {
        JuQueryAO juQueryAO = new JuQueryAO();
        juQueryAO.setGameKind(queryDTO.getGameCode());
        juQueryAO.setOrderNo(queryDTO.getOrderNo());
        juQueryAO.setBarId(barID);
        return new ParamWrapper<JuQueryAO>(juQueryAO);
    }
}
