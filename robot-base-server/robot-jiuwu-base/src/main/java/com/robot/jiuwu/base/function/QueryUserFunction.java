package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;

import com.robot.jiuwu.base.basic.PathEnum;
import com.robot.jiuwu.base.vo.QueryUserResultBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户是否存在，和基本信息
 */
@Slf4j
@Service
public class QueryUserFunction extends AbstractFunction<String,String,QueryUserResultBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.QUERY_USER;
    }

    @Override
    protected IEntity getEntity(String params, RobotWrapper robotWrapper) {
        return JsonEntity.custom(1).add("gameid", params);
    }

    @Override
    protected IResultHandler<String, QueryUserResultBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换：
     * 存在返回：
     *      {"code":0,"IsSuccess":true,}
     * 不存在返回：
     *      {"code":1,"IsSuccess":false,}
     */
    private static final class ResultHandler implements IResultHandler<String, QueryUserResultBO>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, QueryUserResultBO> shr) {
            String result = shr.getOriginalEntity();
            log.info("查询会员存在功能响应：{}", result);
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("未响应");
            }

            QueryUserResultBO usesrResultVO = JSON.parseObject(result, QueryUserResultBO.class);
            if (null == usesrResultVO.getCode()) {
                return Response.FAIL("转换失败");
            }
            return Response.SUCCESS(usesrResultVO);



                
        }
    }

}
