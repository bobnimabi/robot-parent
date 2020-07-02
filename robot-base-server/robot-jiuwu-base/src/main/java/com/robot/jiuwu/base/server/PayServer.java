package com.robot.jiuwu.base.server;

import com.bbin.common.pojo.TaskAtomDto;
import com.robot.center.util.MoneyUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
//import com.robot.gpk.base.ao.PayFinalAO;

import com.robot.jiuwu.base.dto.PayFinalAO;
import com.robot.jiuwu.base.function.PayFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author mrt
 * @Date 2020/6/15 15:25
 * @Version 2.0
 */
@Service
public class PayServer implements IAssemFunction<TaskAtomDto> {

    @Autowired
    private PayFunction payFunction;
    @Autowired
    private DepositTokenFunction tokenFunction;

    /**
     * 异步任务
     * 无需有返回
     * @param paramWrapper 参数包装类
     * @param robotWrapper 机器人
     * @return
     * @throws Exception
     */
    @Override
    public Response doFunction(ParamWrapper<TaskAtomDto> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<String> tokenResult = tokenFunction.doFunction(paramWrapper, robotWrapper);
        if (!tokenResult.isSuccess()) {
            return null;
        }
        String token = tokenResult.getObj();

      //  payFunction.doFunction(createParams(paramWrapper.getObj(),token), robotWrapper);
        return null;
    }

    /**
     * 组装支付参数
     * @param taskAtomDto
     * @param token  //todo payfinalAO
     * @return
     */
    private ParamWrapper<PayFinalAO> createParams(TaskAtomDto taskAtomDto, String token) {
        PayFinalAO payFinalAO = new PayFinalAO();
        payFinalAO.setAccountsString(taskAtomDto.getUsername());
        payFinalAO.setDepositToken(token);
        payFinalAO.setType("5");
        payFinalAO.setIsReal( "false");
        payFinalAO.setPortalMemo(taskAtomDto.getFrontMemo());
        payFinalAO.setMemo(taskAtomDto.getMemo());
        payFinalAO.setAmount( MoneyUtil.formatYuan(taskAtomDto.getPaidAmount()).toString());
        payFinalAO.setAmountString(MoneyUtil.formatYuan(taskAtomDto.getPaidAmount()).toString());
        payFinalAO.setTimeStamp(System.currentTimeMillis() + "");
        payFinalAO.setExteralNo(taskAtomDto.getOutPayNo());

        if (taskAtomDto.getIsAudit()) {
            payFinalAO.setAuditType("Discount");
            payFinalAO.setAudit(MoneyUtil.formatYuan(taskAtomDto.getPaidAmount()).toString());
        } else {
            if (null != taskAtomDto.getMultipleTransaction()) {
                payFinalAO.setAuditType("Discount");
                payFinalAO.setAudit(MoneyUtil.formatYuan(taskAtomDto.getPaidAmount().multiply(new BigDecimal(taskAtomDto.getMultipleTransaction()))).toString());
            } else {
                payFinalAO.setAuditType("None");
            }
        }
        return new ParamWrapper<PayFinalAO>(payFinalAO);
    }
}
