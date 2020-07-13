package com.robot.jiuwu.base.function;
import com.alibaba.fastjson.JSON;
import com.robot.center.mq.MqSenter;
import com.robot.center.util.MoneyUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.basic.PathEnum;
import com.robot.jiuwu.base.ao.PayAO;
import com.robot.jiuwu.base.bo.PayBO;
import com.robot.jiuwu.base.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 付款
 */
@Slf4j
@Service
public class PayFunction extends AbstractFunction<PayAO,String, PayBO> {

    @Autowired
    private MqSenter mqSenter;


    @Override
    public Response<PayBO> doFunction(ParamWrapper<PayAO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<PayBO> response = super.doFunction(paramWrapper, robotWrapper);
        PayAO payFinalAO = paramWrapper.getObj();
        mqSenter.topicPublic("",payFinalAO.getExteralNo(),response,payFinalAO.getAmount());
        return response;
    }

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.PAY;
    }


    @Override
    protected IEntity getEntity(PayAO payAO, RobotWrapper robotWrapper) {
        return JsonEntity.custom(12)
                .add("amount", MoneyUtil.convertToFen(payAO.getAmount())) // 金额
                .add("gameids", payAO.getGameId()) // 游戏ids
                .add("password", DigestUtils.md5DigestAsHex(robotWrapper.getPlatformPassword().getBytes())) // 密码
                .add("remark", payAO.getMemo()) // 备注
                .add("type","2") // 0人工充值 1线上补单 2活动彩金 3补单 6其他
                .add("codingDouble","1") //打码量倍数  新增
                ;
    }

    @Override
    protected IResultHandler<String, PayBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应转换
     * 登录响应：
     *
     */
    private static final class ResultHandler implements IResultHandler<String, PayBO> {
     //   private static final String SUCCESS = "true";
        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, PayBO> shr) {
            String result = shr.getOriginalEntity();
            log.info("打款功能响应：{}", result);
            PayBO payBO = JSON.parseObject(result, PayBO.class);

            if (StringUtils.isEmpty(result)) {
                log.info("打款未有任何响应");
                return Response.FAIL("打款未有任何响应：" + result);
            }
            if (Constant.SUCCESS.equals(payBO.getCode())) {
                return Response.SUCCESS("打款成功");
            } else {
                return Response.FAIL("打款失败：" + result);
            }
        }
    }
}
