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
 * 1.登录前检查
 * 附带作用：会获取一个cookie：ckuuid
 */
@Slf4j
@Service
public class CheckNeedVerifyServer extends FunctionBase<LoginDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.GET, action, null, createLoginParams(robotWrapper), null, LoginCheckParse.INSTANCE, false);
        ResponseResult responseResult = standerHttpResponse.getResponseResult();
        if (HttpStatus.SC_OK == standerHttpResponse.getStatusLine().getStatusCode()) {
            return ResponseResult.SUCCESS_MES("登录前校验：成功");
        }
        return ResponseResult.SUCCESS_MES("登录前校验：失败");
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.CHECK_NEED_VERIFY;
    }

    /**
     * 组装参数
     * callback：
     * userName：手机号
     * pwdType：登录类型，猜测 01：密码登录 02：短信验证码登录
     * _：请求时间戳
     */
    private ICustomEntity createLoginParams(RobotWrapper robot) {
        String reqTime = System.currentTimeMillis() + "";
        return UrlCustomEntity.custom()
                .add("callback", CreateJqueryRandomUtil.doRandom())
                .add("userName", robot.getPlatformAccount())
                .add("pwdType", "02")
                .add("_", reqTime);
    }

    private static final class LoginCheckParse implements IResultParse {
        private static final LoginCheckParse INSTANCE = new LoginCheckParse();

        private LoginCheckParse() {
        }

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            String jsonString = ResultUtil.getJsonString(result);
            JSONObject jsonObject = JSON.parseObject(jsonString);
            Boolean resultCode = jsonObject.getBoolean("resultCode");
            String ckCode = jsonObject.getString("ckCode");
            if (Boolean.FALSE.equals(resultCode) && "2".equals(ckCode)) {
                return ResponseResult.SUCCESS("登录前检查成功");
            }
            return ResponseResult.FAIL("登录前检查失败");
        }
    }
}
