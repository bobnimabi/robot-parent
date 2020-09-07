package com.robot.og.base.function;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.robot.center.mq.MqSenter;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.PayAO;
import com.robot.og.base.basic.PathEnum;

import com.robot.og.base.bo.PayBO;
import com.robot.og.base.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 付款
 */
@Slf4j
@Service
public class PayFunction extends AbstractFunction<PayAO,String, Object> {

    @Autowired
    private MqSenter mqSenter;


    @Override
    public Response<Object> doFunction(ParamWrapper<PayAO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<Object> response = super.doFunction(paramWrapper, robotWrapper);
        PayAO payAO = paramWrapper.getObj();
        mqSenter.topicPublic("",payAO.getExteralNo(),response,new BigDecimal(payAO.getDepositMoney()));
        return response;
    }

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.PAY;
    }


    @Override
    protected IEntity getEntity(PayAO ao, RobotWrapper robotWrapper) {
        return UrlEntity.custom(18)
                .add("type", ao.getType())
                .add("memberId", ao.getMemberId())
                .add("depositMoney", ao.getDepositMoney())
                .add("depositMoneyRemark", ao.getDepositMoneyRemark())
                .add("depositPreStatus", "0")
                .add("depositPre", "1")
                .add("otherPreStatus", "0")
                .add("otherPre", "0")
                .add( "DepositMoneyRemark1",ao.getDepositMoneyRemark1())
                .add("compBetCheckStatus", ao.getCompBetCheckStatus().toString())
                .add("compBet", ao.getCompBet().toString())
                .add("normalStatus", "1")
                .add("depositPro", "2存款优惠")
                .add("token", UUID.randomUUID().toString().replaceAll("-",""))  //生成随机token

                ;


    }

    @Override
    protected IResultHandler<String, Object> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应转换
     *
     */
    private static final class ResultHandler implements IResultHandler<String, Object> {
        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, Object> shr) {
            String result = shr.getOriginalEntity();
            log.info("打款功能响应：{}", result);
            PayBO payBO = JSON.parseObject(result, PayBO.class);

            if (StringUtils.isEmpty(result)) {
                log.info("打款未有任何响应");
                return Response.FAIL("打款未有任何响应：" + result);
            }
            if (!Constant.SUCCESS.equals(payBO.getSuccess())) {
                return Response.FAIL("打款失败：" + result);
            } else {

                return Response.SUCCESS("打款成功");
            }
        }
    }

}
