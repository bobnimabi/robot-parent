package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IPathEnum;
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
import com.robot.jiuwu.base.dto.WithdrawListDTO;
import com.robot.jiuwu.base.vo.WithdrawListResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询出款流水
 */
@Slf4j
@Service
public class WithdrawListServer extends FunctionBase<WithdrawListDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<WithdrawListDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        WithdrawListDTO withdrawListDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(withdrawListDTO), null, Parser.INSTANCE);
        ResponseResult withdrawResponse = standerHttpResponse.getResponseResult();
        if (!withdrawResponse.isSuccess()) {
            return withdrawResponse;
        }
        WithdrawListResultVO entity = (WithdrawListResultVO) withdrawResponse.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        return ResponseResult.SUCCESS(entity);
    }

    @Override
    public IPathEnum getActionEnum() {
        return ActionEnum.WITHDRAW_APPLY_LIST;
    }

    // 组装登录参数
    private ICustomEntity createParams(WithdrawListDTO withdrawListDTO) {
        return JsonCustomEntity.custom()
                .add("account", withdrawListDTO.getAccount())
                .add("applytime_end", withdrawListDTO.getApplytime_end())
                .add("applytime_start", withdrawListDTO.getApplytime_start())
                .add("current", withdrawListDTO.getCurrent() + "")
                .add("gameid", withdrawListDTO.getGameid())
                .add("id", withdrawListDTO.getId())
                .add("isRecharge", withdrawListDTO.getIsRecharge())
                .add("isremark2", withdrawListDTO.getIsremark2())
                .add("lock", withdrawListDTO.getLock())
                .add("memberOrder", withdrawListDTO.getMemberOrder())
                .add("money_start", withdrawListDTO.getMoney_start().toString())
                .add("money_end", withdrawListDTO.getMoney_end().toString())
                .add("name", withdrawListDTO.getName())
                .add("paytype", withdrawListDTO.getPaytype() + "")
                .add("pendingName", withdrawListDTO.getPendingName())
                .add("size", withdrawListDTO.getSize() + "")
                .add("source", withdrawListDTO.getSource())
                .add("status", withdrawListDTO.getStatus())
                .add("updatetime_end", withdrawListDTO.getUpdatetime_end())
                .add("updatetime_start", withdrawListDTO.getUpdatetime_start())
                .add("username", withdrawListDTO.getUsername());
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
            WithdrawListResultVO withdrawListResult = JSON.parseObject(result, WithdrawListResultVO.class);
            if (null == withdrawListResult.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(withdrawListResult);
        }
    }
}
