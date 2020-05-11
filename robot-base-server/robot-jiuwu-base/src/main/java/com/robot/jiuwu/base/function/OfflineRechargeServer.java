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
import com.robot.jiuwu.base.dto.WithdrawListDTO;
import com.robot.jiuwu.base.vo.OfflineRechargeVO;
import com.robot.jiuwu.base.vo.WithdrawListResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 线下充值总金额
 */
@Slf4j
@Service
public class OfflineRechargeServer extends FunctionBase<OfflineDataDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<OfflineDataDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        OfflineDataDTO offlineDataDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(offlineDataDTO), null, Parser.INSTANCE);
        ResponseResult withdrawResponse = standerHttpResponse.getResponseResult();
        if (!withdrawResponse.isSuccess()) {
            return withdrawResponse;
        }
        OfflineRechargeVO entity = (OfflineRechargeVO) withdrawResponse.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        return ResponseResult.SUCCESS(entity);
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.OFFLINE_RECHARGE;
    }

    // 组装登录参数
    private ICustomEntity createParams(OfflineDataDTO offlineDataDTO) {
        return JsonCustomEntity.custom()
                .add("current", String.valueOf(offlineDataDTO.getCurrent()))
                .add("size", String.valueOf(offlineDataDTO.getSize()))
                .add("recordid", offlineDataDTO.getRecordid())
                .add("username", offlineDataDTO.getUsername())
                .add("types", Arrays.toString(offlineDataDTO.getTypes()))
                .add("gameid", offlineDataDTO.getGameid())
                .add("memberOrder", offlineDataDTO.getMemberOrder())
                .add("orderdatebegin", offlineDataDTO.getOrderdatebegin())
                .add("orderdateend", offlineDataDTO.getOrderdateend())
                .add("minamount", MoneyUtil.formatYuan(offlineDataDTO.getMinamount()).toString())
                .add("maxamount", MoneyUtil.formatYuan(offlineDataDTO.getMaxamount()).toString())
                .add("remark", offlineDataDTO.getRemark());
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
            OfflineRechargeVO rechargeVO = JSON.parseObject(result, OfflineRechargeVO.class);
            if (null == rechargeVO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(rechargeVO);
        }
    }
}
