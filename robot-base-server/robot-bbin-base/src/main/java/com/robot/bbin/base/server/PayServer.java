package com.robot.bbin.base.server;

import com.bbin.common.pojo.TaskAtomDto;
import com.robot.bbin.base.ao.PayAO;
import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.bbin.base.function.PayFunction;
import com.robot.bbin.base.function.QueryBalanceFunction;
import com.robot.center.util.MoneyUtil;
import com.robot.code.dto.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 打款：先查后打
 * @Author mrt
 * @Date 2020/6/15 15:12
 * @Version 2.0
 */
@Service
public class PayServer implements IAssemFunction<TaskAtomDto> {

    @Autowired
    private QueryBalanceFunction queryUserFunction;

    @Autowired
    private PayFunction payFunction;

    @Override
    public Response doFunction(ParamWrapper<TaskAtomDto> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<QueryBalanceBO> response = queryUserFunction.doFunction(new ParamWrapper<String>(), robotWrapper);
        if (!response.isSuccess()) {
            return response;
        }
        return payFunction.doFunction(createPayParams(paramWrapper.getObj(), response.getObj()), robotWrapper);
    }

    /**
     * 组装查询余额参数
     * @param paramWrapper
     * @return
     */
    private ParamWrapper<String> createQueryBalanceParams(ParamWrapper<TaskAtomDto> paramWrapper) {
        TaskAtomDto taskAtomDto = paramWrapper.getObj();
        return new ParamWrapper<String>(taskAtomDto.getUsername());
    }

    /**
     * 组装打款参数
     * @param moneyDTO
     * @param balanceVO
     * @return
     * @throws Exception
     */
    private ParamWrapper<PayAO> createPayParams(TaskAtomDto moneyDTO, QueryBalanceBO balanceVO) throws Exception {
        PayAO payDTO = new PayAO();
        payDTO.setUser_name(moneyDTO.getUsername());
        payDTO.setUser_id(balanceVO.getUser_id());
        payDTO.setHallid(balanceVO.getHallID());
        payDTO.setCHK_ID(balanceVO.getCHK_ID());
        payDTO.setUser_name(balanceVO.getUser_name());
        payDTO.setDate(balanceVO.getDate());
        payDTO.setCurrency("RMB");
        payDTO.setAbamount_limit("0");
        payDTO.setAmount(moneyDTO.getPaidAmount().toString());
        payDTO.setAmount_memo(moneyDTO.getMemo());
        payDTO.setCommissionCheck("Y");
        payDTO.setDepositItem("ARD8");
        if (moneyDTO.getIsAudit()) {
            payDTO.setComplexAuditCheck("1");
            payDTO.setComplex(moneyDTO.getPaidAmount().toString());
        } else {
            if (null != moneyDTO.getMultipleTransaction()) {
                payDTO.setComplexAuditCheck("1");
                payDTO.setComplex(MoneyUtil.formatYuan(new BigDecimal(moneyDTO.getMultipleTransaction()).multiply(moneyDTO.getPaidAmount())).toString());
            } else {
                payDTO.setComplex("0");
            }
        }
        return new ParamWrapper<PayAO>(payDTO);
    }
}
