package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.ao.TotalRechargeAO;
import com.robot.jiuwu.base.basic.PathEnum;
import com.robot.jiuwu.base.dto.TotalRechargeDTO;
import com.robot.jiuwu.base.vo.RechargeResultBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 所有游戏打码总量之和
 */
@Slf4j
@Service
public class TotalRechargeDetailFunction extends AbstractFunction<TotalRechargeAO,String, RechargeResultBO> {


    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.TOTAL_RECHARGE_DETAIL;
    }

    @Override
    protected IEntity getEntity(TotalRechargeAO rechargeDTO, RobotWrapper robotWrapper) {

        return JsonEntity.custom(6)
                .add("gameid", rechargeDTO.getGameid())
                .add("start", rechargeDTO.getStart())
                .add("end", rechargeDTO.getEnd());

    }


    @Override
    protected IResultHandler<String, RechargeResultBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, RechargeResultBO> {
        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }


        @Override
        public Response parse2Obj(StanderHttpResponse<String, RechargeResultBO> shr) {
            String result = shr.getOriginalEntity();
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("未响应");
            }
            RechargeResultBO rechargeResultVO = JSON.parseObject(result, RechargeResultBO.class);
            if (null == rechargeResultVO.getCode()) {
                return Response.FAIL("响应结果转换失败");


            }
            return Response.SUCCESS(rechargeResultVO);
        }
    }
}