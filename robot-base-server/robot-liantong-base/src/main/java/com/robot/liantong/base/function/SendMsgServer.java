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
import com.robot.liantong.base.common.Constant;
import com.robot.liantong.base.uril.CreateJqueryRandomUtil;
import com.robot.liantong.base.uril.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 2.联通：登录前短信
 */
@Slf4j
@Service
public class SendMsgServer extends FunctionBase<LoginDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.GET, action, null, createLoginParams(robotWrapper), null, LoginParse.INSTANCE, false);
        ResponseResult responseResult = standerHttpResponse.getResponseResult();
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return CommonActionEnum.SMS;
    }

    /**
     * 参数说明：
     * callback:
     * req_time:请求时间
     * mobile：手机号
     * _:请求时间
     */
    private ICustomEntity createLoginParams(RobotWrapper robot) {
        String reqTime = System.currentTimeMillis() + "";
        return UrlCustomEntity.custom()
                .add("callback", CreateJqueryRandomUtil.doRandom())
                .add("req_time", reqTime)
                .add("mobile", robot.getPlatformAccount())
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
            String resultCode = jsonObject.getString("resultCode");
            if (Constant.SUCCESS.equals(resultCode)) {
                return ResponseResult.SUCCESS("短信验证码发送成功");
            }
            return ResponseResult.FAIL("短信验证码发送失败");
        }
    }
}
