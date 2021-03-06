package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.bbin.base.basic.ActionEnum;
import com.robot.bbin.base.vo.QueryBalanceVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 局查询
 */
@Service
public class QueryBalanceServer extends FunctionBase<String> {

    @Override
    public ResponseResult doFunctionFinal(ParamWrapper<String> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        String userName = paramWrapper.getObj();
        // 执行
        StanderHttpResponse response = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null,
                createQueryBalanceParams(userName), null, QueryBalanceParse.INSTANCE);
        ResponseResult responseResult = response.getResponseResult();
        if (!responseResult.isSuccess()) {
            return responseResult;
        }
        QueryBalanceVO queryBalanceVO = (QueryBalanceVO) responseResult.getObj();
        if (StringUtils.isEmpty(queryBalanceVO.getLoginName())) {
            return ResponseResult.FAIL("会员不存在");
        }
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.QUERY_BALANCE;
    }

    //组装局查询
    private UrlCustomEntity createQueryBalanceParams(String userName) throws Exception{
        return UrlCustomEntity.custom().add("search_name", userName);
    }

    /**
     * 响应结果转换类
     */
    private static final class QueryBalanceParse implements IResultParse {
        private static final QueryBalanceParse INSTANCE = new QueryBalanceParse();
        private QueryBalanceParse(){}

        @Override
        public ResponseResult parse(String result) {
            return ResponseResult.SUCCESS(JSON.parseObject(result, QueryBalanceVO.class));
        }
    }
}
