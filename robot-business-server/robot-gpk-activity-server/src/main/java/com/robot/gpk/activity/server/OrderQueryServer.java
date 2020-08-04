package com.robot.gpk.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.util.DateUtils;
import com.robot.center.util.DateUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.ao.OrderQueryAO;
import com.robot.gpk.base.bo.OrderQueryBO;
import com.robot.gpk.base.function.OrderQueryFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * 局查询（所有平台均可使用）
 * 使用：查询注单
 * 注意：局查询页面必须是：
 *      请选择：注单编码
 * @Author mrt
 * @Date 2020/6/2 16:34
 * @Version 2.0
 *
 */
@Service
public class OrderQueryServer implements IAssemFunction<OrderNoQueryDTO> {



    @Autowired
    private OrderQueryFunction orderQueryFunction;



    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        Response<OrderQueryBO> orderQueryBOResult = orderQueryFunction.doFunction(juQueryAOParams(queryDTO), robotWrapper);
        OrderQueryBO orderQueryBO = orderQueryBOResult.getObj();
        // 校验日期     todo
       /* JuQueryBO juQueryBO = response.getObj();
        if (juQueryBO.getOrderTime().isBefore(queryDTO.getStartDate())) {
            return Response.FAIL("订单已过期,订单号："+juQueryBO.getPlatFormOrderNo());
        }*/
        // 校验会员账号
       /* if (!orderQueryBO.getPageData().get(0).getAccount().equals(queryDTO.getUserName())) {
            return Response.FAIL("会员账号不匹配，传入：" + queryDTO.getUserName() + " 实际：" + orderQueryBO.getPageData().get(0).getAccount());
        }*/
        return  orderQueryBOResult;

    }
    /**
     * 查询 局查询参数组装
     */
    private ParamWrapper<OrderQueryAO>  juQueryAOParams(OrderNoQueryDTO queryDTO) {
        OrderQueryAO orderQueryAO = new OrderQueryAO();
        orderQueryAO.setAccount(queryDTO.getUserName());
        orderQueryAO.setWagersTimeBegin(  queryDTO.getStartDate());
        orderQueryAO.setWagersTimeEnd(queryDTO.getEndDate());
        orderQueryAO.setPayoffTimeEnd(queryDTO.getEndDate());
        orderQueryAO.setRawWagersId(queryDTO.getOrderNo());
        orderQueryAO.setGameCategories(Collections.singletonList("BBINprobability"));
        orderQueryAO.setConnectionId(UUID.randomUUID().toString());

        return new ParamWrapper<OrderQueryAO>(orderQueryAO);


    }

}
