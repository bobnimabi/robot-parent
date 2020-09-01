package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot.bbin.base.ao.BetDetailAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.BetDetailBO;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 下注查询
 */
@Service
public class BetDetailFunction2 extends AbstractFunction<BetDetailAO,String,BetDetailBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.BET_ANALYSIS_DETAIL;
    }

    @Override
    protected IEntity getEntity(BetDetailAO betDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(6)
                .add("listid", betDTO.getListid())
                .add("start", betDTO.getStart())
                .add("end", betDTO.getEnd())
                .add("game", betDTO.getGame())
                .add("currency", betDTO.getCurrency())
                .add("gametype", betDTO.getGametype());
    }

    @Override
    protected IResultHandler<String, BetDetailBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果处理
     */
    private static final class ResultHandler implements IResultHandler<String, BetDetailBO> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, BetDetailBO> shr) {
            String result = shr.getOriginalEntity();

            BetDetailBO betDetailBO = JSON.parseObject(result,BetDetailBO.class);

            return Response.SUCCESS(betDetailBO);
        }
    }
}
