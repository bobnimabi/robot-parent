package com.robot.gpk.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.pojo.TaskAtomDto;
import com.bbin.common.response.CommonCode;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.ICustomEntity;
import com.robot.center.httpclient.JsonCustomEntity;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.mq.MqSenter;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.util.MoneyUtil;
import com.robot.code.entity.TenantRobotAction;
import com.robot.gpk.base.basic.ActionEnum;
import com.robot.gpk.base.dto.PayFinalDTO;
import com.robot.gpk.base.vo.PayResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 支付
 */
@Slf4j
@Service
public class PayFinalServer extends AbstractFunction<PayFinalDTO> {

    @Autowired
    private MqSenter mqSenter;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<PayFinalDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        PayFinalDTO payFinalDTO = paramWrapper.getObj();
        TaskAtomDto taskAtomDto = payFinalDTO.getTaskAtomDto();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, createParams(payFinalDTO, robotWrapper), null, Parse.INSTANCE, false);
        ResponseResult responseResult = standerHttpResponse.getResponseResult();
        if (responseResult.isSuccess()) {
            topicPublic(standerHttpResponse.getRecordId(), taskAtomDto.getOutPayNo(), true, "打款成功", taskAtomDto.getTheme(), taskAtomDto.getPaidAmount());
        } else {
            topicPublic(standerHttpResponse.getRecordId(), taskAtomDto.getOutPayNo(), false, "打款失败", taskAtomDto.getTheme(), taskAtomDto.getPaidAmount());
        }
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.PAY;
    }

    // 组装登录参数
    private ICustomEntity createParams(PayFinalDTO payFinalDTO, RobotWrapper robotWrapper) {

        TaskAtomDto taskAtomDto = payFinalDTO.getTaskAtomDto();
        ICustomEntity entity = JsonCustomEntity.custom()
                .add("AccountsString", taskAtomDto.getUsername()) // 存入帐号
                .add("DepositToken", payFinalDTO.getDepositToken()) // 应该是防止表单重复提交的
                .add("Type", "5") // 类型：人工存提：4 优惠活动：5 返水：6 补发派彩：7 其他 99
                .add("IsReal", "false") // 实际存提，true或false
                .add("PortalMemo", taskAtomDto.getFrontMemo()) // 前台备注
                .add("Memo", taskAtomDto.getMemo()) // 后台备注
                .add("Password", robotWrapper.getPlatformPassword()) // 登录密码
                .add("Amount", MoneyUtil.formatYuan(taskAtomDto.getPaidAmount()).toString()) // 存款金额
                .add("AmountString", MoneyUtil.formatYuan(taskAtomDto.getPaidAmount()).toString()) // 金额字符串
                .add("TimeStamp", System.currentTimeMillis() + "");

        if (taskAtomDto.getIsAudit()) {
            entity.add("AuditType", "Discount"); // 稽核方式 免稽核：None 存款稽核：Deposit 优惠稽核：Discount
            entity.add("Audit", MoneyUtil.formatYuan(taskAtomDto.getPaidAmount().multiply(BigDecimal.ONE)).toString()); // 稽核金额
        } else {
            if (null != taskAtomDto.getMultipleTransaction()) {
                entity.add("AuditType", "Discount");
                entity.add("Audit", MoneyUtil.formatYuan(taskAtomDto.getPaidAmount().multiply(new BigDecimal(taskAtomDto.getMultipleTransaction()))).toString()); // 稽核金额
            } else {
                entity.add("AuditType", "None");
            }
        }
        return entity;
    }

    /**
     * 响应转换
     * 登录响应：
     *  {"IsSuccess":true,"WaitingTime":"\/Date(1588801054323)\/","SendAddress":"+861*******785"}
     */
    private static final class Parse implements IResultParse{
        private static final Parse INSTANCE = new Parse();
        private Parse(){}
        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            if ("true".equals(result)) {
                return ResponseResult.SUCCESS_MES("打款成功");
            } else {
                return ResponseResult.FAIL("打款失败：" + result);
            }
        }
    }

    // 组装响应对象并发布
    private void topicPublic(String robotRecordId, String outPayNo, boolean isSuccess, String errorMes, String theme, BigDecimal paidAmount) {
        //构建响应信息
        PayResponseVO payResponseVo = new PayResponseVO(robotRecordId,outPayNo,paidAmount);
        //使用消息队列通知其他微服务
        ResponseResult resp = null;
        if (!isSuccess) {
            resp = ResponseResult.FAIL_OBJ(errorMes, JSON.toJSONString(payResponseVo));
        } else {
            resp = new ResponseResult(CommonCode.PAY_SUCCESS, JSON.toJSONString(payResponseVo));
        }
        mqSenter.sendMessage(RabbitMqConstants.ROBOT_SUCCESS_EXCHANGE_NAME, RabbitMqConstants.ROBOT_SUCCESS_ROUTE_KEY, resp);
    }
}
