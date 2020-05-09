package com.robot.gpk.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.ICustomEntity;
import com.robot.center.httpclient.JsonCustomEntity;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.gpk.base.basic.ActionEnum;
import com.robot.gpk.base.vo.GeneralVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户是否存在，和基本信息
 */
@Slf4j
@Service
public class QueryUserServer extends FunctionBase<String> {

    @Override
    public ResponseResult doFunctionFinal(ParamWrapper<String> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        String userName = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createQueryUserParams(userName), null, QueryUserParser.INSTANCE);
        ResponseResult queryUserResponse = standerHttpResponse.getResponseResult();
        return queryUserResponse;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.QUERY_USER;
    }

    // 组装登录参数
    private ICustomEntity createQueryUserParams(String userName) {
        return JsonCustomEntity.custom().add("Account", userName);
    }

    /**
     * 响应结果转换：
     * 存在返回：
     *      {"ReturnObject":1,"IsSuccess":true,"ErrorMessage":null}
     * 不存在返回：
     *      {"ReturnObject":0,"IsSuccess":true,"ErrorMessage":null}
     */
    private static final class QueryUserParser implements IResultParse{
        private static final QueryUserParser INSTANCE = new QueryUserParser();
        private QueryUserParser(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            GeneralVO generalVO = JSON.parseObject(result, GeneralVO.class);
            if (generalVO.getIsSuccess()) {
                int i = Integer.parseInt(String.valueOf(generalVO.getReturnObject()));
                if (i == 1) {
                    return ResponseResult.SUCCESS_MES("会员存在");
                } else {
                    return ResponseResult.FAIL("该会员不存在");
                }
            }
            return ResponseResult.FAIL("未知错误：" + JSON.toJSONString(generalVO));
        }
    }
}
