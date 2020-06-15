package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.ResponseBO;
import com.robot.code.dto.LoginDTO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginServer extends AbstractFunction<LoginDTO, String, ResponseBO> {

    /**
     * 这里重写的原因是：登录完成后，需要手动添加特定cookie
     * @param paramWrapper
     * @param robotWrapper
     * @return
     * @throws Exception
     */
    @Override
    public Response<ResponseBO> doFunction(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<ResponseBO> response = super.doFunction(paramWrapper, robotWrapper);
        ResponseBO loginResp = response.getObj();
        if (response.isSuccess() && null != loginResp && true == loginResp.getResult()) {
            this.addCookies(robotWrapper,loginResp.getData().getSession_id());
        }
        return response;
    }

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.LOGIN;
    }

    @Override
    protected IEntity getEntity(LoginDTO loginDTO, RobotWrapper robot) {
        UrlEntity entity = UrlEntity.custom(3)
                .add("username", robot.getPlatformAccount())
                .add("password", robot.getPlatformPassword());
        if (!StringUtils.isEmpty(loginDTO.getOpt())) {
            entity.add("otp", loginDTO.getOpt());
        }
        return entity;
    }

    @Override
    protected IResultHandler<String, ResponseBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    private static final class ResultHandler implements IResultHandler<String, ResponseBO> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler() {}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, ResponseBO> shr) {
            String result = shr.getOriginalEntity();
            JSONObject jsonObject = JSON.parseObject(result);
            Boolean isSuccess = (Boolean) jsonObject.get("result");
            if (!isSuccess) {
                log.error("登录失败：{}", result);
                return Response.FAIL(jsonObject.getString("message"));
            }
            return Response.LOGIN_SUCCESS(JSON.parseObject(result, ResponseBO.class));
        }
    }

    /**
     * 添加特定Cookies
     * @param robotWrapper
     * @param sid
     */
    private void addCookies(RobotWrapper robotWrapper,String sid) {
        CookieStore cookieStore = robotWrapper.getCookieStore();
        String domain = cookieStore.getCookies().get(0).getDomain();
        this.addCookie(cookieStore, "sid", sid, domain);
        this.addCookie(cookieStore, "langx", "zh-cn", domain);
        this.addCookie(cookieStore, "langcode", "zh-cn", domain);
    }

    private void addCookie(CookieStore cookieStore, String key, String value,String domain) {
        BasicClientCookie cookie = new BasicClientCookie(key, value);
        cookie.setDomain(domain);
        cookieStore.addCookie(cookie);
    }
}
