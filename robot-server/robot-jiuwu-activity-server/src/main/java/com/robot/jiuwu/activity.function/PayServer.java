package com.robot.jiuwu.activity.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.response.CommonCode;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.CommonActionEnum;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.mq.MqSenter;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.center.util.MoneyUtil;
import com.robot.code.dto.TenantRobotDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.activity.basic.ActionEnum;
import com.robot.jiuwu.activity.common.Constant;
import com.robot.jiuwu.activity.dto.PayMoneyDTO;
import com.robot.jiuwu.activity.vo.LoginResultVO;
import com.robot.jiuwu.activity.vo.PayResponseVo;
import com.robot.jiuwu.activity.vo.PayResultVO;
import com.robot.jiuwu.activity.vo.QueryUserResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class PayServer extends FunctionBase<PayMoneyDTO> {
    @Autowired
    private QueryUserServer queryUserServer;
    @Autowired
    private MqSenter mqSenter;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<PayMoneyDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        PayMoneyDTO gameDTO = paramWrapper.getObj();

        // 查询余额：查询UserID
        ResponseResult balanceResponse = queryUserServer.doFunctionFinal(new ParamWrapper<String>(gameDTO.getUsername()), robotWrapper, getAction(ActionEnum.QUERY_USER));
        if (!balanceResponse.isSuccess()) {
            return balanceResponse;
        }
        QueryUserResultVO queryUserResultVO = (QueryUserResultVO) balanceResponse.getObj();

        // 执行
        StanderHttpResponse response = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null,
                createBodyParams(gameDTO, queryUserResultVO,robotWrapper), null, ResultParse.INSTANCE);
        ResponseResult responseResult = response.getResponseResult();
        PayResultVO payResultVO = (PayResultVO) responseResult.getObj();
        boolean isSuccess = responseResult.isSuccess() && Constant.SUCCESS.equals(payResultVO.getCode());
        // 发布
        topicPublic(response.getRecordId(), gameDTO.getOutPayNo(), responseResult.isSuccess(), isSuccess ? "打款成功" : responseResult.getMessage() + ":" + payResultVO.getMsg(), gameDTO.getTheme(), gameDTO.getPaidAmount());
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.PAY;
    }

    /**
     * 注释掉的参数，点击“查询”按钮的时候会全部带上，点击“BB电子”是没有的，为了方便临时先去掉
     *
     */
    private UrlCustomEntity createBodyParams(PayMoneyDTO moneyDTO, QueryUserResultVO resultVO, RobotWrapper robotWrapper) throws Exception {
        UrlCustomEntity customEntity = UrlCustomEntity.custom()
                .add("amount", MoneyUtil.convertToFen(moneyDTO.getPaidAmount()).toString()) // 金额
                .add("codingDouble","0") // 打码量倍数
                .add("gameids", resultVO.getData().getGameid() + "") // 游戏ids
                .add("password", DigestUtils.md5DigestAsHex(robotWrapper.getPlatformPassword().getBytes())) // 密码
                .add("remark", moneyDTO.getMemo()) // 备注
                .add("type","2") // 0人工充值 1线上补单 2活动彩金 3补单 6其他
                ;
        return customEntity;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultParse implements IResultParse {
        private static final ResultParse INSTANCE = new ResultParse();
        private ResultParse(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            PayResultVO payResultVO = JSON.parseObject(result, PayResultVO.class);
            if (null == payResultVO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(payResultVO);
        }
    }

    // 组装响应对象并发布
    private void topicPublic(String robotRecordId, String outPayNo, boolean isSuccess, String errorMes, String theme, BigDecimal paidAmount) {
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
