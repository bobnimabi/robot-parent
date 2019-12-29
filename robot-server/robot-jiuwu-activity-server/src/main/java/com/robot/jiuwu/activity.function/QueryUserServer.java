package com.robot.jiuwu.activity.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.activity.basic.ActionEnum;
import com.robot.jiuwu.activity.common.Constant;
import com.robot.jiuwu.activity.vo.ImageCodeResultVO;
import com.robot.jiuwu.activity.vo.QueryUserResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户是否存在
 */
@Slf4j
@Service
public class QueryUserServer extends FunctionBase<String> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<String> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        String userId = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createQueryUserParams(userId), null, QueryUserParser.INSTANCE);
        ResponseResult queryUserResponse = standerHttpResponse.getResponseResult();
        if (!queryUserResponse.isSuccess()) {
            return queryUserResponse;
        }
        QueryUserResultVO entity = (QueryUserResultVO) queryUserResponse.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        return ResponseResult.SUCCESS(entity);
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.QUERY_USER;
    }

    // 组装登录参数
    private UrlCustomEntity createQueryUserParams(String userId) {
        return UrlCustomEntity.custom().add("gameid", userId);
    }

    // 响应结果转换
    private static final class QueryUserParser implements IResultParse{
        private static final QueryUserParser INSTANCE = new QueryUserParser();
        private QueryUserParser(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            QueryUserResultVO usesrResultVO = JSON.parseObject(result, QueryUserResultVO.class);
            if (null == usesrResultVO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(usesrResultVO);
        }
    }
}
