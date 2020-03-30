package com.robot.liantong.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.CommonActionEnum;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 3.校验用户是否存在
 */
@Slf4j
@Service
public class CheckAgreementServer extends FunctionBase<LoginDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null, createLoginParams(robotWrapper), null, LoginParse.INSTANCE, false);
        ResponseResult responseResult = standerHttpResponse.getResponseResult();

        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.CHECK_AGREEMENT;
    }

    /**
     * 参数说明
     * 1.callback
     * 2.userId：机器人账号
     * 3.userType：01普通用户
     * 4._:请求时间
     */
    private ICustomEntity createLoginParams(RobotWrapper robot) {
        String reqTime = System.currentTimeMillis() + "";
        return UrlCustomEntity.custom()
                .add("callback", CreateJqueryRandomUtil.doRandom())
                .add("userId", robot.getPlatformAccount())
                .add("userType", "01")
                .add("_", reqTime);
    }

    // 响应结果转换
    private static final class LoginParse implements IResultParse {
        private static final LoginParse INSTANCE = new LoginParse();

        private LoginParse() {
        }

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            String jsonString = ResultUtil.getJsonString(result);
            JSONObject jsonObject = JSON.parseObject(jsonString);
            Boolean resultCode = jsonObject.getBoolean("resultCode");
            if (Boolean.TRUE.equals(resultCode)) {
                return ResponseResult.SUCCESS("登录前：校验用户是否存在，成功");
            }
            return ResponseResult.FAIL("登录前：校验用户是否存在，失败");
        }
    }
}
