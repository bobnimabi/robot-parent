package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.ICustomEntity;
import com.robot.center.httpclient.JsonCustomEntity;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.dto.TotalRechargeDTO;
import com.robot.jiuwu.base.vo.RechargeResultVO;
import com.robot.jiuwu.base.basic.ActionEnum;
import com.robot.jiuwu.base.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询区间下注详情（按游戏来分）
 */
@Slf4j
@Service
public class TotalRechargeDetailServer extends FunctionBase<TotalRechargeDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<TotalRechargeDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        TotalRechargeDTO rechargeDTO = paramWrapper.getObj();

        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(rechargeDTO), null, ResultParse.INSTANCE);
        ResponseResult queryUserResponse = standerHttpResponse.getResponseResult();
        if (!queryUserResponse.isSuccess()) {
            return queryUserResponse;
        }
        RechargeResultVO entity = (RechargeResultVO) queryUserResponse.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        return ResponseResult.SUCCESS(entity);
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.TOTAL_RECHARGE_DETAIL;
    }

    // 组装登录参数

    /**
     * currentPage: 1
     * end: "2019-12-30 00:00:00"
     * gameid: "342027"
     * pageSize: 10
     * start: "2019-12-01 00:00:00"
     * total: 0
     */
    private ICustomEntity createParams(TotalRechargeDTO rechargeDTO) {
        return JsonCustomEntity.custom()
                .add("gameid", rechargeDTO.getUserName())
                .add("start", rechargeDTO.getBeginDate())
                .add("end", rechargeDTO.getEndDate())
                .add("total", "0")
                .add("pageSize", "100")
                .add("currentPage", "1");
    }

    // 响应结果转换
    private static final class ResultParse implements IResultParse{
        private static final ResultParse INSTANCE = new ResultParse();
        private ResultParse(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            RechargeResultVO rechargeResultVO = JSON.parseObject(result, RechargeResultVO.class);
            if (null == rechargeResultVO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(rechargeResultVO);
        }
    }
}
