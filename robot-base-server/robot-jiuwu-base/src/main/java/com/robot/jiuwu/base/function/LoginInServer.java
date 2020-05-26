package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.constant.RobotConsts;
import com.robot.center.execute.CommonActionEnum;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.http.CustomHttpMethod;
import com.robot.center.http.ICustomEntity;
import com.robot.center.http.JsonCustomEntity;
import com.robot.center.http.StanderHttpResponse;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.tenant.TContext;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.common.Constant;
import com.robot.jiuwu.base.vo.LoginResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginInServer extends FunctionBase<LoginDTO> {

    @Autowired
    private StringRedisTemplate redis;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        LoginDTO robotDTO = paramWrapper.getObj();
        // 获取验证码的的CaptchaToken
        String captchaToken = redis.opsForValue().get(ImageCodeServer.createCacheKeyCaptchaToken(robotWrapper.getId()));
        if (StringUtils.isEmpty(captchaToken)) {
            return ResponseResult.FAIL("缓存验证码过期");
        }
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createLoginParams(robotWrapper, robotDTO, captchaToken), null, LoginParse.INSTANCE, false);
        ResponseResult loginResponse = standerHttpResponse.getResponseResult();
        if (!loginResponse.isSuccess()) {
            return loginResponse;
        }
        LoginResultVO entity = (LoginResultVO) loginResponse.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        // 保存token
        redis.opsForValue().set(createCacheKeyLoginToken(robotWrapper.getId()), entity.getData().getToken(), Duration.ofDays(1));
        return ResponseResult.SUCCESS();
    }

    @Override
    public IActionEnum getActionEnum() {
        return CommonActionEnum.LOGIN;
    }

    // 组装登录参数
    private ICustomEntity createLoginParams(RobotWrapper robot, LoginDTO robotDTO, String captchaToken) {
        ICustomEntity entity = JsonCustomEntity.custom()
                .add("username", robot.getPlatformAccount())
                .add("password", DigestUtils.md5DigestAsHex(robot.getPlatformPassword().getBytes()))
                .add("captcha", robotDTO.getImageCode())
                .add("captchaToken", captchaToken)
                .add("code", robotDTO.getOpt());
        return entity;
    }

    // 响应结果转换
    private static final class LoginParse implements IResultParse{
        private static final LoginParse INSTANCE = new LoginParse();
        private LoginParse(){}
        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            LoginResultVO loginResult = JSON.parseObject(result, LoginResultVO.class);
            if (null == loginResult.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(loginResult);
        }
    }

    // 创建机器人的登录TOKEN
    public static String createCacheKeyLoginToken(long robotId) {
        return new StringBuilder(50)
                .append(RobotConsts.LOGIN_TOKEN)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(robotId)
                .toString();
    }
}
