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
import com.robot.jiuwu.base.dto.UpdateWithdrawStatusDTO;
import com.robot.jiuwu.base.vo.UpdateWithdrawStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 更新出款流水的状态
 *
 */
@Slf4j
@Service
public class UpdateWithdrawStatusServer extends FunctionBase<UpdateWithdrawStatusDTO> {
    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<UpdateWithdrawStatusDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        UpdateWithdrawStatusDTO updateWithdrawStatusDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(updateWithdrawStatusDTO), null, Parser.INSTANCE);
        ResponseResult withdrawResponse = standerHttpResponse.getResponseResult();
        if (!withdrawResponse.isSuccess()) {
            return withdrawResponse;
        }
        UpdateWithdrawStatusVO entity = (UpdateWithdrawStatusVO) withdrawResponse.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        return ResponseResult.SUCCESS(entity);
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.UPDATE_WITHDRAW_STATUS;
    }

    // 组装登录参数:多个ids以英文逗号分隔
    // status:订单状态：0审核中 1审核通过 2预出款 3出款中 4已成功
    // -1已退回 -2已拒绝 5代付 6代付中
    private ICustomEntity createParams(UpdateWithdrawStatusDTO updateWithdrawStatusDTO) {
        return JsonCustomEntity.custom()
                .add("ids", String.join(",", updateWithdrawStatusDTO.getIds()))
                .add("status", updateWithdrawStatusDTO.getStatus() + "");
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
            UpdateWithdrawStatusVO updateWithdrawStatusVO = JSON.parseObject(result, UpdateWithdrawStatusVO.class);
            if (null == updateWithdrawStatusVO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(updateWithdrawStatusVO);
        }
    }
}
