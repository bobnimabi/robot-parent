package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.http.CustomHttpMethod;
import com.robot.center.http.ICustomEntity;
import com.robot.center.http.JsonCustomEntity;
import com.robot.center.http.StanderHttpResponse;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.basic.ActionEnum;
import com.robot.jiuwu.base.common.Constant;
import com.robot.jiuwu.base.dto.DoLockDTO;
import com.robot.jiuwu.base.vo.DoLockVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 出款：锁
 */
@Slf4j
@Service
public class DoLockServer extends FunctionBase<DoLockDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<DoLockDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        DoLockDTO doLockDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(doLockDTO), null, Parser.INSTANCE);
        ResponseResult doLockResponse = standerHttpResponse.getResponseResult();
        if (!doLockResponse.isSuccess()) {
            return doLockResponse;
        }
        DoLockVO entity = (DoLockVO) doLockResponse.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        return ResponseResult.SUCCESS(entity);
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.DO_LOCK;
    }

    // 组装登录参数:多个ids以英文逗号分隔
    private ICustomEntity createParams(DoLockDTO doLockDTO) {
        return JsonCustomEntity.custom()
                .add("id", doLockDTO.getId() + "");
    }

    // 响应结果转换
    private static final class Parser implements IResultParse{
        private static final Parser INSTANCE = new Parser();
        private Parser(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            DoLockVO doLockVO = JSON.parseObject(result, DoLockVO.class);
            if (null == doLockVO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(doLockVO);
        }
    }
}
