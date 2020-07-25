package com.robot.gpk.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.bo.JuQueryBO;
import com.robot.gpk.base.function.JuQueryFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private JuQueryFunction juQueryFunction;


    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        //局查询  
        Response<JuQueryBO> response = juQueryFunction.doFunction(paramWrapper, robotWrapper);
        if (!response.isSuccess()) {
            return response;
        }
        // 校验日期
        JuQueryBO juQueryBO = response.getObj();
        if (juQueryBO.getOrderTime().isBefore(queryDTO.getStartDate())) {
            return Response.FAIL("订单已过期,订单号：" + juQueryBO.getPlatFormOrderNo());
        }
        // 校验会员账号
        if (!juQueryBO.getUserName().equals(queryDTO.getUserName())) {
            return Response.FAIL("会员账号不匹配，传入：" + queryDTO.getUserName() + " 实际：" + juQueryBO.getUserName());
        }
        return response;


    }
}