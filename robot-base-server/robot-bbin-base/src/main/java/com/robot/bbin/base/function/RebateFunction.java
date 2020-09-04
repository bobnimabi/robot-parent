package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.bbin.base.ao.BetAO2;
import com.robot.bbin.base.ao.RebateAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.rebate.Premium_data;
import com.robot.bbin.base.bo.rebate.Rebate;
import com.robot.bbin.base.bo.rebate.RebateBean;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 查询已申请的优惠总额
 */
@Slf4j
@Service
public class RebateFunction extends AbstractFunction<RebateAO,String, String> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.BET_ANALYSIS;
    }

    @Override
    protected IEntity getEntity(RebateAO dto, RobotWrapper robotWrapper) {
        return UrlEntity.custom(8)
                .add("start", dto.getStart())
                .add("end", dto.getEnd())
                .add("premium", "0")
                .add("amount", "")
                .add("premium_than1", "0")
                .add("times", "")
                .add("premium_than2", "0")
                .add("Currency", "RMB")
                .add("sortCol", "1")
                .add("sort", "0")
                .add("name", dto.getName())
                .add("accountType", "member")
                .add("analystType", "member")
                .add("page", "1");

    }

    @Override
    protected IResultHandler<String,String > getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 结果转换
     */
    private static final class ResultHandler implements IResultHandler<String,String> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, String> shr) {
            String result = shr.getOriginalEntity();
            log.info( result);
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("优惠查询:未响应结果");
            }
            RebateBean  rebateBean = JSON.parseObject(result, RebateBean.class);
            String premiumAmount;
            premiumAmount=null== rebateBean.getRebate()? "0":rebateBean.getRebate().getPremium_data().get(0).getPremiumAmount();
            return Response.SUCCESS(premiumAmount);
        }
    }
}
