package com.robot.og.base.function;
import com.alibaba.fastjson.JSON;
import com.robot.center.constant.RobotConsts;
import com.robot.code.dto.LoginDTO;
import com.robot.code.response.Response;
import com.robot.code.response.ResponseEnum;
import com.robot.code.service.ITenantRobotDomainService;
import com.robot.core.common.TContext;
import com.robot.core.function.base.*;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.basic.PathEnum;
import com.robot.og.base.bo.LoginResultVO;
import com.robot.og.base.common.Constant;
import com.robot.og.base.server.ImageCodeServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by mrt on 11/14/2019 8:06 PM    COOKIE还要加
 * 登录
 */
@Slf4j
@Service
public class LoginFunction extends AbstractFunction<LoginDTO, String, LoginResultVO> {


    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.LOGIN;
    }

    @Override
    protected IEntity getEntity(LoginDTO loginDTO, RobotWrapper robot ) {

        return UrlEntity.custom(3)
                .add("robotId",loginDTO.getId().toString())
                .add("imageCode",loginDTO.getImageCode())
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

    private static final class ResultHandler implements IResultHandler<String, LoginResultVO> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler() {}

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
            return Response.FAIL("姿势不对,请重新登录!");

        }
    }


/*
    public static void main(String[] args) throws IOException {

        Document document = Jsoup.parse(new File("E:\\project\\robot-parent\\robot-business-server\\robot-og-activity-server\\src\\main\\resources\\test.html"), "utf-8");
        Element loginForm = document.getElementById("loginForm");
        if (StringUtils.isEmpty(loginForm)){
            System.out.println("登录成功");
        }
        System.out.println("loginForm = " + loginForm);

    }

*/


}
