package com.robot.liantong.base.function;

import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.ICustomEntity;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.liantong.base.basic.ActionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 9.买卡提交前：参数校验
 */
@Slf4j
@Service
public class BuyCardCheckServer extends FunctionBase<LoginDTO> {

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.GET, action, null, createLoginParams(robotWrapper), null, IsLoginParse.INSTANCE, false);
        if (HttpStatus.SC_OK == standerHttpResponse.getStatusLine().getStatusCode()) {
            return ResponseResult.SUCCESS_MES("已登录");
        }
        return ResponseResult.FAIL("已掉线");
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.BUY_CARD_CHECK;
    }

    /**
     * 参数说明
     * cardBean.cardValueCode：卡类型，  01:20元卡   02:30元卡   03:50元卡   04：100元卡
     * offerPriceStrHidden：hidden卡面值（字符串形式）
     * offerRateStrHidden：hidden：默认：1，应该可以写死（可能来自于前一个接口）
     * cardBean.cardValue：卡面值
     * cardBean.minCardNum：最小卡数量1，写死即可
     * cardBean.maxCardNum：最多卡数量3（注意，根据面值会变化，最大金额不超过300）
     * MaxThreshold01：hidden，表示20元卡最多买15张，默认：15
     * MinThreshold01：hidden，表示20元的卡最少买1张，默认：1
     * MaxThreshold02：hidden，表示30元的卡最多买10张，默认：10
     * MinThreshold02：hidden，表示30元的卡最少买1张，默认：1
     * MaxThreshold03：hidden，表示50元的卡最多买6张，默认：6
     * MinThreshold03：hidden，表示50元的卡最少买1张，默认：1
     * MaxThreshold04：hidden，表示100元的卡最多买3张，默认：3
     * MinThreshold04：hidden，表示100元的卡最少买1张，默认：1
     * commonBean.channelType：猜测：渠道类型，101
     * secstate.state：猜测：防表单重复提交
     * cardBean.buyCardAmount：购卡数量，默认1
     * cardBean.buyCardPhoneNo：购卡手机号
     * phoneVerifyCode：短信验证码
     */
    private ICustomEntity createLoginParams(RobotWrapper robot) {
        String reqTime = System.currentTimeMillis() + "";
        return UrlCustomEntity.custom()
                .add("cardBean.cardValueCode", "04")
                .add("offerPriceStrHidden", "100.00")
                .add("offerRateStrHidden", "1")
                .add("cardBean.cardValue", "100")
                .add("cardBean.minCardNum", "1")
                .add("cardBean.maxCardNum", "3")
                .add("MaxThreshold01", "15")
                .add("MinThreshold01", "1")
                .add("MaxThreshold02", "10")
                .add("MinThreshold02", "1")
                .add("MaxThreshold03", "6")
                .add("MinThreshold03", "1")
                .add("MaxThreshold04", "3")
                .add("MinThreshold04", "1")
                .add("commonBean.channelType", "101")
                .add("secstate.state", "")
                .add("cardBean.buyCardAmount", "1")
                .add("cardBean.buyCardPhoneNo", robot.getPlatformAccount())
                // TODO 短信校验码，由上一个接口带过来
                .add("phoneVerifyCode", "");
    }

    // 响应结果转换
    private static final class IsLoginParse implements IResultParse {
        private static final IsLoginParse INSTANCE = new IsLoginParse();
        private IsLoginParse() {}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            return ResponseResult.SUCCESS("确认是否登录：成功");
        }
    }
}
