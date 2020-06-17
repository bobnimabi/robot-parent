package com.robot.bbin.activity.server;

import com.bbin.common.dto.robot.BreakThroughDTO;
import com.robot.core.function.base.IAssemFunction;
import com.robot.bbin.base.ao.InOutCashAO;
import com.robot.bbin.base.bo.InOutCashBO;
import com.robot.bbin.base.function.InOutCashFunction;
import com.robot.code.response.Response;
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
public class QueryRechargeServer implements IAssemFunction<BreakThroughDTO> {

    @Autowired
    private InOutCashFunction inOutCashServer;

    @Override
    public Response<InOutCashBO> doFunction(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<InOutCashBO> betDetailResult = inOutCashServer.doFunction(new ParamWrapper<InOutCashAO>(createRechargeParams(paramWrapper.getObj())), robotWrapper);
        if (!betDetailResult.isSuccess()) {
            return betDetailResult;
        }
        return betDetailResult;
    }

    /**
     * 查询充值参数
     */
    private InOutCashAO createRechargeParams(BreakThroughDTO breakThroughDTO)throws Exception {
        InOutCashAO in = new InOutCashAO();
        in.setStart(getDate(breakThroughDTO.getBeginDate()));
        in.setEnd(getDate(breakThroughDTO.getEndDate()));
        in.setMethed("0");
        in.setName(breakThroughDTO.getUserName());
        return in;
    }

    private String getDate(String dateTime) {
        return dateTime.substring(0, dateTime.indexOf(" "));
    }
}
