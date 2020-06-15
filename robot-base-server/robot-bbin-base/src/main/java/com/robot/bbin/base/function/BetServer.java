package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.gpk.base.ao.BetAO;
import com.robot.bbin.base.bo.BetBO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 下注查询
 */
@Service
public class BetServer extends AbstractFunction<BetAO,String,List<BetBO>> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.BET_ANALYSIS;
    }

    @Override
    protected ICustomEntity getEntity(BetAO betDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(8)
                .add("start", betDTO.getStart())
                .add("end", betDTO.getEnd())
                .add("game", betDTO.getGame())
                .add("currency", betDTO.getCurrency())
                .add("gametype", betDTO.getGametype())
                .add("name", betDTO.getName())
                .add("accountType", betDTO.getAccountType())
                .add("analystType", betDTO.getAnalystType());
    }

    @Override
    protected IResultHandler<String, List<BetBO>> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 结果转换
     */
    private static final class ResultHandler implements IResultHandler<String,List<BetBO>> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, List<BetBO>> shr) {
            String result = shr.getOriginalEntity();
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("下注查询:未响应结果");
            }
            if (result.contains("error")) {
                return Response.FAIL(result);
            }
            List<BetBO> betVOS = JSON.parseArray(result, BetBO.class);
            return Response.SUCCESS(betVOS);
        }
    }
}
