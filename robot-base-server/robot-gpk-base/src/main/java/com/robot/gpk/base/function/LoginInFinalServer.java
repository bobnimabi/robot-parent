package com.robot.gpk.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.CommonActionEnum;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.ICustomEntity;
import com.robot.center.httpclient.JsonCustomEntity;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.gpk.base.vo.GeneralVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginInFinalServer extends AbstractFunction<LoginDTO> {

    @Autowired
    private StringRedisTemplate redis;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        LoginDTO loginDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createLoginParams(robotWrapper, loginDTO), null, LoginParse.INSTANCE, false);
        ResponseResult loginResponse = standerHttpResponse.getResponseResult();
        return ResponseResult.SUCCESS();
    }

    @Override
    public IActionEnum getActionEnum() {
        return CommonActionEnum.LOGIN;
    }

    // 组装登录参数
    private ICustomEntity createLoginParams(RobotWrapper robot, LoginDTO loginDTO) {
        ICustomEntity entity = JsonCustomEntity.custom()
                .add("account", robot.getPlatformAccount())
                .add("password", robot.getPlatformPassword());
        return entity;
    }

    /**
     * 响应转换
     * 登录响应：
     *  {"IsSuccess": true,"Methods": null}
     *  {"ReturnObject": null,"IsSuccess": false,"ErrorMessage": "您已经登入，无需再次登入"}
     */
    private static final class LoginParse implements IResultParse{
        private static final LoginParse INSTANCE = new LoginParse();
        private LoginParse(){}
        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            GeneralVO loginResultVo = JSON.parseObject(result, GeneralVO.class);
            if (!loginResultVo.getIsSuccess()) {
                return ResponseResult.FAIL(loginResultVo.getErrorMessage());
            }
            log.info("登录响应：{}", result);
            return ResponseResult.SUCCESS();
        }
    }

}
