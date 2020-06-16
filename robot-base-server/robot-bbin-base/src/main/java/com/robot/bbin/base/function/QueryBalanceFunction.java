package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 局查询
 */
@Service
public class QueryBalanceFunction extends AbstractFunction<String,String, QueryBalanceBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.QUERY_BALANCE;
    }

    @Override
    protected IEntity getEntity(String params, RobotWrapper robotWrapper) {
        return UrlEntity.custom(1).add("search_name", params);
    }

    @Override
    protected IResultHandler<String, QueryBalanceBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, QueryBalanceBO> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, QueryBalanceBO> shr) {
            String result = shr.getOriginalEntity();
            QueryBalanceBO qbVO = JSON.parseObject(result, QueryBalanceBO.class);
            if (StringUtils.isEmpty(qbVO.getLoginName())) {
                return Response.FAIL("会员不存在");
            }
            return Response.SUCCESS(qbVO);
        }
    }
}
