package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
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

/**
 * 局查询（BB电子）
 * 查询注单
 * @Author mrt
 * @Date 2020/6/2 16:34
 * @Version 2.0
 *
 */
@Service
public class OrderQueryServer implements IAssemFunction<OrderNoQueryDTO> {

    @Autowired
    private JuQueryFunction juQueryServer;

    @Override
    public Response<JuQueryBO> doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();
        Response<JuQueryBO> response = juQueryServer.doFunction(juQueryAO(queryDTO), robotWrapper);
        if (!response.isSuccess()) {
            return response;
        }
        JuQueryBO juQueryBO = response.getObj();
        if (StringUtils.isEmpty(juQueryBO.getPageId())) {
            return Response.FAIL("订单已过期,订单号："+juQueryBO.getPlatFormOrderNo());
        }
        if (!queryDTO.getUserName().equals(juQueryBO.getUserName())) {
            return Response.FAIL("会员账号不匹配，传入：" + queryDTO.getUserName() + " 实际：" + juQueryBO.getUserName());
        }
        return response;
    }

    /**
     * 局查询参数组装
     */
    private ParamWrapper<JuQueryAO> juQueryAO(OrderNoQueryDTO queryDTO) {
        JuQueryAO juQueryAO = new JuQueryAO();
        juQueryAO.setGameKind(queryDTO.getGameCode());
        juQueryAO.setOrderNo(queryDTO.getOrderNo());
        return new ParamWrapper<JuQueryAO>(juQueryAO);
    }
}
