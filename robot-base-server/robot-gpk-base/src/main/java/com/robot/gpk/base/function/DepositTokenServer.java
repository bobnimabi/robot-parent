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
import com.robot.center.util.MoneyUtil;
import com.robot.code.entity.TenantRobotAction;
import com.robot.gpk.base.basic.ActionEnum;
import com.robot.gpk.base.dto.PayMoneyDTO;
import com.robot.gpk.base.vo.SmsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 付款前：获取Token（防表单重复提交）
 */
@Slf4j
@Service
public class DepositTokenServer extends FunctionBase<PayMoneyDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<PayMoneyDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        PayMoneyDTO payMoneyDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, null, null, Parse.INSTANCE, false);
        ResponseResult responseResult = standerHttpResponse.getResponseResult();
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.DEPOSIT_TOKEN;
    }

    /**
     * 响应转换
ss     */
    private static final class Parse implements IResultParse{
        private static final Parse INSTANCE = new Parse();
        private Parse(){}
        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            return ResponseResult.SUCCESS_MES(result.substring(1,result.length()-1));
        }
    }
}
