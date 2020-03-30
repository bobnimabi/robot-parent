package com.robot.liantong.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.ICustomEntity;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.liantong.base.basic.ActionEnum;
import com.robot.liantong.base.uril.CreateJqueryRandomUtil;
import com.robot.liantong.base.uril.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 5+7判断是否登录
 * 买卡提交前：获取和校验短信验证码
 */
@Slf4j
@Service
public class IsLoginServer extends FunctionBase<LoginDTO> {
    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.GET, action, null, createLoginParams(robotWrapper), null, IsLoginParse.INSTANCE, false);
        if (HttpStatus.SC_OK == standerHttpResponse.getStatusLine().getStatusCode()) {
            return ResponseResult.SUCCESS_MES("已登录");
        }
        return ResponseResult.FAIL("已掉线");
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.IS_LOGIN;
    }

    private ICustomEntity createLoginParams(RobotWrapper robot) {
        String reqTime = System.currentTimeMillis() + "";
        return UrlCustomEntity.custom()
                .add("redirectKey", "BuyCardLoginReturnUrl")
                .add("time", Math.random() + "")
                .add("callback", CreateJqueryRandomUtil.doRandom())
                .add("_", reqTime);
    }

    // 响应结果转换
    private static final class IsLoginParse implements IResultParse {
        private static final IsLoginParse INSTANCE = new IsLoginParse();
        private IsLoginParse() {}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            String jsonString = ResultUtil.getJsonString(result);
            JSONObject jsonObject = JSON.parseObject(jsonString);
            Boolean isLogin = jsonObject.getBoolean("isLogin");
            if (Boolean.TRUE.equals(isLogin)) {
                return ResponseResult.SUCCESS("确认是否登录：已登录");
            }
            return ResponseResult.FAIL("确认是否登录：未登录");
        }
    }
}
