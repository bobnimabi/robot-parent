package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IPathEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.http.CustomHttpMethod;
import com.robot.center.http.ICustomEntity;
import com.robot.center.http.JsonCustomEntity;
import com.robot.center.http.StanderHttpResponse;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.vo.UserInfoDetailResultVO;
import com.robot.jiuwu.base.basic.ActionEnum;
import com.robot.jiuwu.base.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户明细
 */
@Slf4j
@Service
public class QueryUserDetailServer extends FunctionBase<String> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<String> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        String userId = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(userId), null, Parser.INSTANCE);
        ResponseResult userInfoResult = standerHttpResponse.getResponseResult();
        if (!userInfoResult.isSuccess()) {
            return userInfoResult;
        }
        UserInfoDetailResultVO entity = (UserInfoDetailResultVO) userInfoResult.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        return ResponseResult.SUCCESS(entity);
    }

    @Override
    public IPathEnum getActionEnum() {
        return ActionEnum.QUERY_USER_DETAIL;
    }

    // 组装登录参数
    private ICustomEntity createParams(String userId) {
        return JsonCustomEntity.custom().add("gameid", userId);
    }

    // 响应结果转换
    private static final class Parser implements IResultParse{
        private static final Parser INSTANCE = new Parser();
        private Parser(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            UserInfoDetailResultVO usesrDetailResultVO = JSON.parseObject(result, UserInfoDetailResultVO.class);
            if (null == usesrDetailResultVO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(usesrDetailResultVO);
        }
    }
}
