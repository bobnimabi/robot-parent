package com.robot.og.base.function;


import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotDomain;
import com.robot.code.response.Response;
import com.robot.code.response.ResponseEnum;
import com.robot.core.function.base.*;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.basic.PathEnum;
import com.robot.og.base.bo.LoginResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginFunction extends AbstractFunction<LoginDTO,String, LoginResultVO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.LOGIN;
    }


    @Override
    protected IEntity getEntity(LoginDTO loginDTO, RobotWrapper robot ) throws Exception {
        return UrlEntity.custom(6)
                .add("account",accountEncrypt(robot.getPlatformAccount()))  // 账号加密
                .add("password",passwordEncrypt(robot.getPlatformPassword()))  //robot.getPlatformPassword()
                .add("dynamicPassword",loginDTO.getOpt())  //动态口令
                .add("type","agentLogin")    //固定参数
                .add("rmNum",loginDTO.getImageCode())   //图片验证码
                .add("systemLanguage","zh_CN")
                ;
    }

    /**
     * 注意：与登录相关的接口，返回null，不进行掉线检查
     */
    @Override
    protected ICheckLost getCHECK_LOST_SERVICE() {
        return null;
    }


    @Override
    protected IResultHandler<String, LoginResultVO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应转换
     * 登录响应：
     *
     */
    private static final class ResultHandler implements IResultHandler<String, LoginResultVO> {

        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, LoginResultVO> shr) {
            String result = shr.getOriginalEntity();
            log.info("登录响应：{}", result);

            //解析结果有登录表单  id=loginForm  表示跳转到登录页面
            Document document = Jsoup.parse(result);
            Element loginForm = document.getElementById("loginForm");
            if (StringUtils.isEmpty(loginForm)){
                return Response.SUCCESS(ResponseEnum.LOGIN_SUCCESS,"登录成功");
            }
            return Response.FAIL("登录失败,请重新登录!");

        }
    }

    //获取登录账号的加密串
    private String accountEncrypt(String account) throws Exception{
        return new String(Base64Utils.encode(account.getBytes())).trim();
    }

    //获取登录密码的加密串
    private String passwordEncrypt(String platPassword) throws Exception{
        return new String(Base64Utils.encode(DigestUtils.md5DigestAsHex(platPassword.trim().getBytes()).getBytes()));
    }



    private void addCookies(RobotWrapper robotWrapper,String sid) throws MalformedURLException {
        CookieStore cookieStore = robotWrapper.getCookieStore();
      //  TenantRobotDomain domain = domainService.getDomain(1);
    //    URL url = new URL(domain.getDomain());
    //    String host = url.getHost();
       // this.addCookie(cookieStore, "sid", sid, host);
      //  this.addCookie(cookieStore, "langx", "zh-cn", host);
    //    this.addCookie(cookieStore, "langcode", "zh-cn", host);
    }

    private void addCookie(CookieStore cookieStore, String key, String value,String domain) {
        BasicClientCookie cookie = new BasicClientCookie(key, value);
     //   cookie.setDomain(domain);
        cookieStore.addCookie(cookie);
    }



}
