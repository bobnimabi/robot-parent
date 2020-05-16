package com.robot.gpk.activity.function;

import com.bbin.common.pojo.TaskAtomDto;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.gpk.base.basic.ActionEnum;
import com.robot.gpk.base.dto.PayFinalDTO;
import com.robot.gpk.base.function.DepositTokenServer;
import com.robot.gpk.base.function.PayFinalServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 打款
 */
@Slf4j
@Service
public class PayServer extends FunctionBase<TaskAtomDto> {

    @Autowired
    private DepositTokenServer depositTokenServer;

    @Autowired
    private PayFinalServer payFinalServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<TaskAtomDto> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        TaskAtomDto taskAtomDto = paramWrapper.getObj();
        ResponseResult responseResult = depositTokenServer.doFunction(paramWrapper, robotWrapper);
        if (!responseResult.isSuccess()) {
            return ResponseResult.FAIL("打款前：获取 depositToken失败");
        }
        PayFinalDTO payFinalDTO = new PayFinalDTO();
        payFinalDTO.setDepositToken(responseResult.getMessage());
        payFinalDTO.setTaskAtomDto(taskAtomDto);

        ResponseResult responseResult1 = payFinalServer.doFunction(new ParamWrapper<PayFinalDTO>(payFinalDTO), robotWrapper);
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.PAY;
    }
}
