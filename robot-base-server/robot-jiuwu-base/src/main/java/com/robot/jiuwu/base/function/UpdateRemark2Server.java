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
import com.robot.jiuwu.base.dto.UpdateRemark2DTO;
import com.robot.jiuwu.base.vo.UpdateRemark2VO;
import com.robot.jiuwu.base.vo.UpdateWithdrawStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 出款：更改二次备注
 */
@Slf4j
@Service
public class UpdateRemark2Server extends FunctionBase<UpdateRemark2DTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<UpdateRemark2DTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        UpdateRemark2DTO updateRemar2kDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(updateRemar2kDTO), null, Parser.INSTANCE);
        ResponseResult updateRemark2Response = standerHttpResponse.getResponseResult();
        if (!updateRemark2Response.isSuccess()) {
            return updateRemark2Response;
        }
        UpdateWithdrawStatusVO entity = (UpdateWithdrawStatusVO) updateRemark2Response.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        return ResponseResult.SUCCESS(entity);
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.UPDATE_REMARK2;
    }

    // 组装登录参数:多个ids以英文逗号分隔
    private ICustomEntity createParams(UpdateRemark2DTO updateRemark2DTO) {
        return JsonCustomEntity.custom()
                .add("id", updateRemark2DTO.getId() + "")
                .add("remark2", updateRemark2DTO.getRemark2());
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
            UpdateRemark2VO updateRemark2VO = JSON.parseObject(result, UpdateRemark2VO.class);
            if (null == updateRemark2VO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(updateRemark2VO);
        }
    }
}
