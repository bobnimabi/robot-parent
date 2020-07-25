package com.robot.og.base.server;

import com.bbin.common.pojo.TaskAtomDto;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;

import com.robot.og.base.ao.PayAO;
import com.robot.og.base.ao.QueryUserAO;
import com.robot.og.base.bo.QueryUserBO;
import com.robot.og.base.function.PayFunction;
import com.robot.og.base.function.QueryUserFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 打款：先查后打
 * @Author mrt
 * @Date 2020/6/15 15:12
 * @Version 2.0
 */
@Service
public class PayServer implements IAssemFunction<TaskAtomDto> {

    @Autowired
    private QueryUserFunction queryUserFunction;

    @Autowired
    private PayFunction payFunction;

    @Override
    public Response doFunction(ParamWrapper<TaskAtomDto> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<QueryUserBO> response = queryUserFunction.doFunction(createQueryUserParams(paramWrapper), robotWrapper);
        if (!response.isSuccess()) {
            return response;
        }
        return payFunction.doFunction(createPayParams(paramWrapper.getObj(), response.getObj()), robotWrapper);
    }

    /**
     * 组装查询用户参数
     * @param paramWrapper
     * @return
     */
    private ParamWrapper<QueryUserAO> createQueryUserParams(ParamWrapper<TaskAtomDto> paramWrapper) {
        TaskAtomDto taskAtomDto = paramWrapper.getObj();
        QueryUserAO queryUserAO = new QueryUserAO();
        queryUserAO.setType("queryManDeposit");
        queryUserAO.setAccount(taskAtomDto.getUsername());
        return new ParamWrapper<QueryUserAO>(queryUserAO);
    }

    /**
     * 组装打款参数
     * @param moneyDTO
     * @param
     * @return
     * @throws Exception
     */
    private ParamWrapper<PayAO> createPayParams(TaskAtomDto moneyDTO, QueryUserBO queryUserBO  ) throws Exception {

        PayAO payAO = new PayAO();
        payAO.setType("saveSet");
        payAO.setMemberId(queryUserBO.getMemberId());
        payAO.setDepositMoney(moneyDTO.getPaidAmount().toString());
        payAO.setDepositMoneyRemark(moneyDTO.getMemo());
        payAO.setDepositPreStatus("0");
        payAO.setDepositPre(Math.random());
        payAO.setOtherPreStatus("0");
        payAO.setOtherPre("0");
        //是否稽核打码量
        if(moneyDTO.getIsAudit()){
            payAO.setCompBetCheckStatus(1);
            payAO.setCompBet(1);
        }else {
            if(moneyDTO.getMultipleTransaction()==null){
                payAO.setCompBetCheckStatus(0);
                payAO.setCompBet(0);
            }else {

                payAO.setCompBetCheckStatus(1);
                payAO.setCompBet(moneyDTO.getMultipleTransaction());
            }

        }


        payAO.setNormalStatus("1");
        payAO.setDepositPro("1");
        payAO.setToken(UUID.randomUUID().toString().replaceAll("-",""));
        payAO.setDepositMoneyRemark1(moneyDTO.getMemo());  //todo 客户想传入订单号

        return new ParamWrapper<PayAO>(payAO);


    }
}
