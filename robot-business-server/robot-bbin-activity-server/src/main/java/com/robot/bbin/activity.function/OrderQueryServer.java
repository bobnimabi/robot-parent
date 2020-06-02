package com.robot.bbin.activity.function;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.function.JuQueryServer;
import com.robot.code.dto.Response;
import com.robot.core.function.base.IFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @Author mrt
 * @Date 2020/6/2 16:34
 * @Version 2.0
 * 局查询（BB电子）
 */
public class OrderQueryServer implements IFunction<OrderNoQueryDTO,Object, JuQueryBO> {

    @Autowired
    private JuQueryServer juQueryServer;

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
