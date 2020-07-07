package com.robot.jiuwu.base.function;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot.center.constant.RobotConsts;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotDomain;
import com.robot.code.response.Response;
import com.robot.code.service.ITenantRobotDomainService;
import com.robot.core.common.TContext;
import com.robot.core.function.base.*;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.basic.PathEnum;
import com.robot.jiuwu.base.vo.LoginResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginFunction extends AbstractFunction<LoginDTO, String, LoginResultVO> {
    @Autowired
    private ITenantRobotDomainService domainService;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private com.robot.jiuwu.base.server.ImageCodeServer ImageCodeServer;

    /**
     * 这里重写的原因是：登录完成后，需要手动添加特定cookie
     */
    @Override
    public Response<LoginResultVO> doFunction(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

        // 获取验证码的的CaptchaToken
        String captchaToken = redis.opsForValue().get(ImageCodeServer.createCacheKeyCaptchaToken(robotWrapper.getId()));
        if (StringUtils.isEmpty(captchaToken)) {
            return Response.FAIL("缓存验证码过期");
        }


  Response<LoginResultVO> response = super.doFunction(paramWrapper, robotWrapper);
       // LoginResultVO loginResp = response.getObj();
        if (!response.isSuccess()) {
          return Response.FAIL();
        }





        // 保存token
     //   redis.opsForValue().set(createCacheKeyLoginToken(robotWrapper.getId()), imageVO.getData().getToken(), Duration.ofDays(1));


        return Response.SUCCESS();

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
                .add("code", loginDTO.getOpt())
                .add("captchaToken", loginDTO.getCaptchaToken());



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
            JSONObject jsonObject = JSON.parseObject(result);
            Boolean isSuccess = (Boolean) jsonObject.get("result");
            if (!isSuccess) {
                return Response.FAIL(jsonObject.getString("message"));
            }

            LoginResultVO loginResult = JSON.parseObject(result, LoginResultVO.class);
            if (null == loginResult.getCode()) {
                return Response.FAIL("转换失败");
            }
            return Response.SUCCESS(loginResult);
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
}
