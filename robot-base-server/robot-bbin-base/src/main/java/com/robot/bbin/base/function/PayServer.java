package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.pojo.TaskAtomDto;
import com.bbin.common.response.CommonCode;
import com.bbin.common.response.ResponseResult;
import com.robot.bbin.base.basic.ActionEnum;
import com.robot.bbin.base.vo.PayResponseVo;
import com.robot.bbin.base.vo.QueryBalanceVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.mq.MqSenter;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.util.MoneyUtil;
import com.robot.code.entity.TenantRobotAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 充值
 */
@Slf4j
@Service
public class PayServer extends FunctionBase<TaskAtomDto> {
    @Autowired
    private QueryBalanceServer queryBalanceServer;
    @Autowired
    private MqSenter mqSenter;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<TaskAtomDto> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        TaskAtomDto gameDTO = paramWrapper.getObj();

        // 查询余额：查询UserID
        ResponseResult balanceResponse = queryBalanceServer.doFunctionFinal(new ParamWrapper<String>(gameDTO.getUsername()), robotWrapper, getAction(ActionEnum.QUERY_BALANCE));
        if (!balanceResponse.isSuccess()) {
            return balanceResponse;
        }
        QueryBalanceVO queryBalanceVO = (QueryBalanceVO) balanceResponse.getObj();

        // 执行
        StanderHttpResponse response = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null,
                createBodyParams(gameDTO, queryBalanceVO), null, ResultParse.INSTANCE);
        ResponseResult responseResult = response.getResponseResult();
        boolean isSuccess = responseResult.isSuccess();
        // 发布
        topicPublic(response.getRecordId(), gameDTO.getOutPayNo(), responseResult.isSuccess(), isSuccess ? "打款成功" : responseResult.getMessage(), gameDTO.getTheme(), gameDTO.getPaidAmount());
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.PAY;
    }

    /**
     * 注释掉的参数，点击“查询”按钮的时候会全部带上，点击“BB电子”是没有的，为了方便临时先去掉
     * .add("BarID", gameDTO.getBarId())
     * .add("GameKind", gameDTO.getGameKind())
     * .add("GameType", "-1") // -1表示全部
     * .add("Limit", "100")
     * .add("Sort", "DESC")
     */
    private UrlCustomEntity createBodyParams(TaskAtomDto moneyDTO, QueryBalanceVO balanceVO) throws Exception {
        UrlCustomEntity customEntity = UrlCustomEntity.custom()
                .add("search_name", moneyDTO.getUsername())
                .add("user_id", balanceVO.getUser_id())
                .add("hallid", balanceVO.getHallID())
                .add("CHK_ID", balanceVO.getCHK_ID())
                .add("user_name", moneyDTO.getUsername())
                .add("date", balanceVO.getDate())
                .add("Currency", "RMB")
                .add("abamount_limit", "0")
                .add("amount", moneyDTO.getPaidAmount().toString())//存款金额
                .add("amount_memo", moneyDTO.getMemo())//备注
                .add("CommissionCheck", "Y")//
                .add("DepositItem", "ARD8");//存入项目 ARD8表示活动优惠，其他的见网页
        if (moneyDTO.getIsAudit()) {
            customEntity.add("ComplexAuditCheck", "1");//综合打码量稽核  1稽核，不传不稽核
            customEntity.add("complex", moneyDTO.getPaidAmount().toString());//综合打码量（金额）
        } else {
            if (null != moneyDTO.getMultipleTransaction()) {
                customEntity.add("ComplexAuditCheck", "1");//综合打码量稽核  1稽核，不传不稽核
                customEntity.add("complex", MoneyUtil.formatYuan(new BigDecimal(moneyDTO.getMultipleTransaction()).multiply(moneyDTO.getPaidAmount())).toString());
            } else {
                customEntity.add("complex", "0");//综合打码量（金额）
            }
        }
        return customEntity;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultParse implements IResultParse {
        private static final Pattern PATTERN = Pattern.compile("alert\\(([\\s\\S]*)\\)");
        private static final ResultParse INSTANCE = new ResultParse();
        private ResultParse(){}

        @Override
        public ResponseResult parse(String result) {
            log.info("打款响应html：{}", result);
            Matcher matcher = PATTERN.matcher(result);
            if (matcher.find()) {
                String reason = matcher.group(1);
                return ResponseResult.FAIL(reason);
            }
            return ResponseResult.SUCCESS();
        }
    }

    // 组装响应对象并发布
    private void topicPublic(String robotRecordId,String outPayNo,boolean isSuccess,String errorMes,String theme,BigDecimal paidAmount) {
        //构建响应信息
        PayResponseVo payResponseVo = new PayResponseVo(robotRecordId,outPayNo,paidAmount);
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
