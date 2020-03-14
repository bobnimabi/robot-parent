package com.robot.bbin.activity.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bbin.common.response.ResponseResult;
import com.robot.bbin.activity.vo.RobotResponse;
import com.robot.center.execute.CommonActionEnum;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.http.CustomHttpMethod;
import com.robot.center.http.StanderHttpResponse;
import com.robot.center.http.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.net.URI;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginInServer extends FunctionBase<LoginDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        LoginDTO robotDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null, createLoginParams(robotWrapper, robotDTO), null, LoginParse.INSTANCE, false);
        ResponseResult loginResponse = standerHttpResponse.getResponseResult();
        if (!loginResponse.isSuccess()) {
            return loginResponse;
        }
        RobotResponse entity = (RobotResponse) loginResponse.getObj();

        // 登录成功：设置Cookie
        if (HttpStatus.SC_OK == standerHttpResponse.getStatusLine().getStatusCode() && entity.getResult() == true) {
            String host = new URI(action.getActionUrl()).getHost();
            CookieStore cookieStore = robotWrapper.getCookieStore();
            cookieStore.addCookie(createSidCookie(entity,host));
            cookieStore.addCookie(createLangxCookie(host));
            cookieStore.addCookie(createLangCodeCookie(host));
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL(entity.getMessage());
    }

    @Override
    public IActionEnum getActionEnum() {
        return CommonActionEnum.LOGIN;
    }

    // 组装登录参数
    private UrlCustomEntity createLoginParams(RobotWrapper robot, LoginDTO robotDTO) {
        UrlCustomEntity entity = UrlCustomEntity.custom()
                .add("username", robot.getPlatformAccount())
                .add("password", robot.getPlatformPassword());
        if (!StringUtils.isEmpty(robotDTO.getOpt())) {
            entity.add("otp", robotDTO.getOpt());
        }
        return entity;
    }

    // 组装cookie:sid
    private Cookie createSidCookie(RobotResponse entity,String host) {
        BasicClientCookie cookie = new BasicClientCookie("sid", entity.getData().getSession_id());
        cookie.setDomain(host);
        return cookie;
    }

    // 组装cookie:langx
    private Cookie createLangxCookie(String host) {
        BasicClientCookie cookie = new BasicClientCookie("langx", "zh-cn");
        cookie.setDomain(host);
        return cookie;
    }

    // 组装cookie:langcode
    private Cookie createLangCodeCookie(String host) {
        BasicClientCookie cookie = new BasicClientCookie("langcode", "zh-cn");
        cookie.setDomain(host);
        return cookie;
    }

    // 响应结果转换
    private static final class LoginParse implements IResultParse{
        private static final LoginParse INSTANCE = new LoginParse();
        private LoginParse(){}
        @Override
        public ResponseResult parse(String result) {
            JSONObject jsonObject = JSON.parseObject(result);
            Boolean isSuccess = (Boolean)jsonObject.get("result");
            if (!isSuccess) {
                log.error("登录失败：{}",result);
                return ResponseResult.FAIL( jsonObject.getString("message"));
            }
            return ResponseResult.SUCCESS(JSON.parseObject(result, RobotResponse.class));
        }
    }
}
