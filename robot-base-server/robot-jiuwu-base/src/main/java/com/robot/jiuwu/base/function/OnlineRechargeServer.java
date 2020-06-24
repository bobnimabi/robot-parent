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
import com.robot.center.util.MoneyUtil;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.basic.ActionEnum;
import com.robot.jiuwu.base.common.Constant;
import com.robot.jiuwu.base.dto.OfflineDataDTO;
import com.robot.jiuwu.base.dto.OnlineRechargeDTO;
import com.robot.jiuwu.base.vo.OfflineRechargeVO;
import com.robot.jiuwu.base.vo.OnlineRechargeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 线上充值总金额
 */
@Slf4j
@Service
public class OnlineRechargeServer extends FunctionBase<OnlineRechargeDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<OnlineRechargeDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        OnlineRechargeDTO onlineRechargeDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(onlineRechargeDTO), null, Parser.INSTANCE);
        ResponseResult responseResult = standerHttpResponse.getResponseResult();
        if (!responseResult.isSuccess()) {
            return responseResult;
        }
        OnlineRechargeVO entity = (OnlineRechargeVO) responseResult.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        return ResponseResult.SUCCESS(entity);
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.ONLINE_RECHARGE;
    }

    // 组装登录参数
    private ICustomEntity createParams(OnlineRechargeDTO onlineRechargeDTO) {
        BigDecimal amount_start = onlineRechargeDTO.getAmount_start();
        BigDecimal amount_end = onlineRechargeDTO.getAmount_end();
        return JsonCustomEntity.custom()
                .add("current", onlineRechargeDTO.getCurrent() + "")
                .add("size", onlineRechargeDTO.getSize() + "")
                .add("orderid", onlineRechargeDTO.getOrderid())
                .add("gameid", onlineRechargeDTO.getGameid())
                .add("amount_start", null != amount_start ? MoneyUtil.formatYuan(amount_start).toString() : "")
                .add("amount_end", null != amount_end ? MoneyUtil.formatYuan(amount_end).toString() : "")
                .add("orderstatus", onlineRechargeDTO.getOrderstatus())
                .add("paydate_start", onlineRechargeDTO.getPaydate_start())
                .add("paydate_end", onlineRechargeDTO.getPaydate_end())
                .add("orderdate_start", onlineRechargeDTO.getOrderdate_start())
                .add("orderdate_end", onlineRechargeDTO.getOrderdate_end())
                .add("sh", onlineRechargeDTO.getSh())
                .add("source", onlineRechargeDTO.getSource())
                .add("ordertype", onlineRechargeDTO.getOrdertype())
                .add("shareid", onlineRechargeDTO.getShareid())
                .add("onlineid", onlineRechargeDTO.getOnlineid())
                .add("payaddress", onlineRechargeDTO.getPayaddress())
                .add("memberOrder", onlineRechargeDTO.getMemberOrder());
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
            OnlineRechargeVO rechargeVO = JSON.parseObject(result, OnlineRechargeVO.class);
            if (null == rechargeVO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(rechargeVO);
        }
    }
}
