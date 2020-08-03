package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.ResponseBO;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotDomain;
import com.robot.code.response.Response;
import com.robot.code.response.ResponseEnum;
import com.robot.code.service.ITenantRobotDomainService;
import com.robot.core.function.base.*;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginFunction extends AbstractFunction<LoginDTO, String, ResponseBO> {
    @Autowired
    private ITenantRobotDomainService domainService;

    /**
     * 这里重写的原因是：登录完成后，需要手动添加特定cookie
     */
    @Override
    public Response<ResponseBO> doFunction(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Response<ResponseBO> response = super.doFunction(paramWrapper, robotWrapper);
        ResponseBO loginResp = response.getObj();
        if (response.isSuccess()) {
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

        //OG
      /* return JsonEntity.custom(3)
                .add("username", robot.getPlatformAccount())
                .add("password", robot.getPlatformPassword());*/



        //BBIN

  UrlEntity entity = UrlEntity.custom(3)
                .add("username", robot.getPlatformAccount())
                .add("password", robot.getPlatformPassword());
        if (!StringUtils.isEmpty(loginDTO.getOpt())) {
            entity.add("otp", loginDTO.getOpt());
        }
        return entity;
    }

    /**
     * 注意：与登录相关的接口，返回null，不进行掉线检查
     */
    @Override
    protected ICheckLost getCHECK_LOST_SERVICE() {
        return null;
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
            log.info("登录响应：{}", result);
            JSONObject jsonObject = JSON.parseObject(result);
            Boolean isSuccess = (Boolean) jsonObject.get("result");
            if (!isSuccess) {
                return Response.FAIL(jsonObject.getString("message"));
            }
            ResponseBO responseBO = JSON.parseObject(result, ResponseBO.class);
            return Response.SUCCESS(ResponseEnum.LOGIN_SUCCESS, responseBO);
        }
    }

    /**
     * 添加特定Cookies
     * @param robotWrapper
     * @param sid
     */
    private void addCookies(RobotWrapper robotWrapper,String sid) throws MalformedURLException {
        CookieStore cookieStore = robotWrapper.getCookieStore();
        TenantRobotDomain domain = domainService.getDomain(1);
        URL url = new URL(domain.getDomain());
        String host = url.getHost();
        this.addCookie(cookieStore, "sid", sid, host);
        this.addCookie(cookieStore, "langx", "zh-cn", host);
        this.addCookie(cookieStore, "langcode", "zh-cn", host);
    }

    private void addCookie(CookieStore cookieStore, String key, String value,String domain) {
        BasicClientCookie cookie = new BasicClientCookie(key, value);
        cookie.setDomain(domain);
        cookieStore.addCookie(cookie);
    }
}
