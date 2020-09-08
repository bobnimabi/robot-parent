package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.InOutCashAO;
import com.robot.bbin.base.bo.InOutCashBO;
import com.robot.bbin.base.function.InOutCashFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 查询充值金额
 * @Author mrt
 * @Date 2020/6/2 19:18
 * @Version 2.0
 */
@Service
public class RechargeServer implements IAssemFunction<OrderNoQueryDTO> {

    @Autowired
    private InOutCashFunction inOutCashServer;

    @Override
    public Response<InOutCashBO> doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<InOutCashBO> betDetailResult = inOutCashServer.doFunction(new ParamWrapper<InOutCashAO>(createRechargeParams(paramWrapper.getObj())), robotWrapper);
        if (!betDetailResult.isSuccess()) {
            return betDetailResult;
        }
        return betDetailResult;
    }

    /**
     * 查询充值参数
     */
    private InOutCashAO createRechargeParams(OrderNoQueryDTO dto)throws Exception {
        InOutCashAO in = new InOutCashAO();
        in.setStart(DateUtils.getYyyyMMdd(dto.getStartDate()));
        in.setEnd(DateUtils.getYyyyMMdd(dto.getEndDate()));
        in.setMethed("0");
        in.setName(dto.getUserName());
        return in;
    }


}
