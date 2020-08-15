package com.robot.gpk.base.function;

import com.robot.center.mq.MqSenter;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.ao.PayFinalAO;
import com.robot.gpk.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 付款
 */
@Slf4j
@Service
public class PayFunction extends AbstractFunction<PayFinalAO,String,Object> {

    @Autowired
    private MqSenter mqSenter;

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.PAY;
    }

    @Override
    public Response<Object> doFunction(ParamWrapper<PayFinalAO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<Object> response = super.doFunction(paramWrapper, robotWrapper);
        PayFinalAO payFinalAO = paramWrapper.getObj();
        mqSenter.topicPublic("",payFinalAO.getExteralNo(),response,new BigDecimal(payFinalAO.getAmount()));
        return response;
    }

    @Override
    protected IEntity getEntity(PayFinalAO params, RobotWrapper robotWrapper) {
        return JsonEntity.custom(12)
                .add("AccountsString", params.getAccountsString())
                .add("DepositToken", params.getDepositToken())
                .add("Type", params.getType())
                .add("IsReal", params.getIsReal())
                .add("PortalMemo", params.getPortalMemo())
                .add("Memo", params.getMemo())
                .add("Password", robotWrapper.getPlatformPassword())
                .add("Amount", params.getAmount())
                .add("AmountString", params.getAmountString())
                .add("TimeStamp", params.getTimeStamp())
                .add("AuditType", params.getAuditType())
                .add("Audit",params.getAudit());
    }


    @Override
    protected IResultHandler<String, Object> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应转换
     * 登录响应：
     * {"IsSuccess":true,"WaitingTime":"\/Date(1588801054323)\/","SendAddress":"+861*******785"}
     */
    private static final class ResultHandler implements IResultHandler<String, Object> {
        private static final String SUCCESS = "true";
        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, Object> shr) {
            String result = shr.getOriginalEntity();
            log.info("打款功能响应：{}", result);
            if (StringUtils.isEmpty(result)) {
                log.info("打款未有任何响应");
                return Response.FAIL("打款未有任何响应：" + result);
            }
            if (SUCCESS.equals(result)) {
                return Response.SUCCESS("打款成功");
            } else {
                return Response.FAIL("打款失败：" + result);
            }
        }
    }
}
