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
import com.robot.gpk.base.basic.ActionEnum;
import com.robot.gpk.base.dto.SmsDTO;
import com.robot.gpk.base.vo.GeneralVO;
import com.robot.gpk.base.vo.SmsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 发送短信验证码
 */
@Slf4j
@Service
public class ValidateSmsServer extends FunctionBase<LoginDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        LoginDTO smsDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(smsDTO), null, Parse.INSTANCE, false);
        ResponseResult responseResult = standerHttpResponse.getResponseResult();
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.VALIDATE_SMS;
    }

    // 组装登录参数
    private ICustomEntity createParams(LoginDTO smsDTO) {
        ICustomEntity entity = JsonCustomEntity.custom()
                .add("method", "0")
                .add("pinCode", smsDTO.getSms());
        return entity;
    }

    /**
     * 响应转换
     * 校验失败：
     *  {"ReturnObject":null,"IsSuccess":false,"ErrorMessage":"验证码错误，请重新输入"}
     */
    private static final class Parse implements IResultParse{
        private static final Parse INSTANCE = new Parse();
        private Parse(){}
        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            GeneralVO smsVO = JSON.parseObject(result, GeneralVO.class);
            if (smsVO.getIsSuccess()) {
                return ResponseResult.SUCCESS_MES("短信校验成功");
            }
            return ResponseResult.FAIL(smsVO.getErrorMessage());
        }

    }
}
