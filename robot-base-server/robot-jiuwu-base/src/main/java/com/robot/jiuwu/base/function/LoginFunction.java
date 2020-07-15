package com.robot.jiuwu.base.function;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bbin.common.response.ResponseResult;
import com.robot.center.constant.RobotConsts;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotDomain;
import com.robot.code.response.Response;
import com.robot.code.response.ResponseEnum;
import com.robot.code.service.ITenantRobotDomainService;
import com.robot.core.common.TContext;
import com.robot.core.function.base.*;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.basic.PathEnum;
import com.robot.jiuwu.base.common.Constant;
import com.robot.jiuwu.base.server.ImageCodeServer;
import com.robot.jiuwu.base.vo.LoginResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginFunction extends AbstractFunction<LoginDTO, String, LoginResultVO> {

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private com.robot.jiuwu.base.server.ImageCodeServer ImageCodeServer;

    /**
     * 登录完成后，需要手动添加特定cookie
     */
    @Override
    public Response<LoginResultVO> doFunction(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {



        // 获取验证码的的CaptchaToken
        String captchaToken = redis.opsForValue().get( ImageCodeServer.createCacheKeyCaptchaToken(robotWrapper.getId()));
        if (StringUtils.isEmpty(captchaToken)) {
            return Response.FAIL("缓存验证码 captchaToken 过期");
        }

        LoginDTO obj = paramWrapper.getObj();
        obj.setCaptchaToken(captchaToken);

        Response<LoginResultVO> loginResponse = super.doFunction(paramWrapper, robotWrapper);
        if (!loginResponse.isSuccess()) {
            return Response.FAIL(loginResponse.getMessage());
        }
        LoginResultVO entity = loginResponse.getObj();

        if (Constant.SUCCESS.equals(entity.getCode())) {
            //添加cookie
            this.addCookies(robotWrapper,entity.getData().getToken());
        }

        // 保存token
     //  redis.opsForValue().set(createCacheKeyLoginToken(robotWrapper.getId()), entity.getData().getToken(), Duration.ofDays(1));

        return loginResponse;

    }

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.LOGIN;
    }

    @Override
    protected IEntity getEntity(LoginDTO loginDTO, RobotWrapper robot ) {

        return JsonEntity.custom(5)
                .add("username", robot.getPlatformAccount())
                .add("password",  DigestUtils.md5DigestAsHex(robot.getPlatformPassword().getBytes()))
                .add("captcha", loginDTO.getImageCode())
                .add("captchaToken",loginDTO.getCaptchaToken() )  //robot.getProperties().getProperty("captchaToken")
                .add("code", loginDTO.getOpt());

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
           // JSONObject jsonObject = JSON.parseObject(result);

            LoginResultVO loginResult = JSON.parseObject(result, LoginResultVO.class);
            if (Constant.LOSE .equals(loginResult.getCode()) ) {
                return Response.FAIL(loginResult.getMsg());

            }
            return Response.SUCCESS(ResponseEnum.LOGIN_SUCCESS,loginResult);
        }
    }



    // 创建机器人的登录TOKEN
    public static String createCacheKeyLoginToken(long robotId) {
        return new StringBuilder(50)
                .append(RobotConsts.CAPTCHA_TOKEN)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(robotId)
                .toString();
    }




    /**
     * 添加特定Cookies
     * @param robotWrapper
     * @param
     */
    private void addCookies(RobotWrapper robotWrapper,String  game_admin_token) throws MalformedURLException {
        CookieStore cookieStore = robotWrapper.getCookieStore();
        this.addCookie(cookieStore, "game_admin_token", game_admin_token);

    }

    private void addCookie(CookieStore cookieStore, String key, String value) {  //,String domain
        BasicClientCookie cookie = new BasicClientCookie(key, value);
        cookieStore.addCookie(cookie);
    }




}
