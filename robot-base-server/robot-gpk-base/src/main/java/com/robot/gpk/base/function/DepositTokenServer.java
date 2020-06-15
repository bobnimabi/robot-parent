package com.robot.gpk.base.function;

import com.bbin.common.pojo.TaskAtomDto;
import com.bbin.common.response.ResponseResult;
import com.robot.core.function.base.AbstractFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 付款前：获取Token（防表单重复提交）
 */
@Slf4j
@Service
public class DepositTokenServer extends AbstractFunction<TaskAtomDto,String,String> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<TaskAtomDto> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        TaskAtomDto taskAtomDto = paramWrapper.getObj();
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
