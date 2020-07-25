package com.robot.gpk.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.basic.PathEnum;
import com.robot.gpk.base.bo.GeneralBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户是否存在，和基本信息
 */
@Slf4j
@Service
public class QueryUserFunction extends AbstractFunction<String,String,Object> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.QUERY_USER;
    }

    @Override
    protected IEntity getEntity(String gameid, RobotWrapper robotWrapper) {
        return JsonEntity.custom(1).add("gameid", gameid);
    }

    @Override
    protected IResultHandler<String, Object> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换：
     * 存在返回：
         */
    private static final class ResultHandler implements IResultHandler<String,Object>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, Object> shr) {
            String result = shr.getOriginalEntity();
            log.info("查询会员存在功能响应：{}", result);
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("未响应");
            }
            GeneralBO generalVO = JSON.parseObject(result, GeneralBO.class);
            if (generalVO.getIsSuccess()) {
                int i = Integer.parseInt(String.valueOf(generalVO.getReturnObject()));
                if (i == 1) {
                    return Response.SUCCESS("会员存在");
                } else {
                    return Response.FAIL("会员不存在");
                }
            }
            return Response.FAIL("未知错误：" + JSON.toJSONString(generalVO));
        }
    }
}
