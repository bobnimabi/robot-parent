package com.robot.liantong.base.function;

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
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 8.买卡前:校验手机短信
 */
@Slf4j
@Service
public class CheckPhoneVerifyCodeServer extends FunctionBase<LoginDTO> {

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
        return ActionEnum.CHECK_PHONE_VERIFY_CODE;
    }

    private ICustomEntity createLoginParams(RobotWrapper robot) {
        String reqTime = System.currentTimeMillis() + "";
        return UrlCustomEntity.custom()
                .add("callback", "checkSuccess")
                .add("commonBean.phoneNo", robot.getPlatformAccount())
                // TODO 短信验证码
                .add("phoneVerifyCode", "短信验证码")
                .add("timeStamp", Math.random() + "")
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
            return ResponseResult.SUCCESS("确认是否登录：成功");
        }
    }
}
