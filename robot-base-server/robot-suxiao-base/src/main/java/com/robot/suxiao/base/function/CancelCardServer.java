package com.robot.suxiao.base.function;

import bom.bbin.common.dto.income.robot.CancelCardDTO;
import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.*;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.suxiao.base.basic.ActionEnum;
import com.robot.suxiao.base.common.Constant;
import com.robot.suxiao.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import static com.robot.suxiao.base.function.MainServer.createCacheKeyCsrf;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 销卡（单张）
 */
@Slf4j
@Service
public class CancelCardServer extends FunctionBase<CancelCardDTO> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<CancelCardDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        CancelCardDTO cancelCardDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null, createParams(cancelCardDTO, robotWrapper.getId()), null, Parser.INSTANCE);
        return standerHttpResponse.getResponseResult();
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.CANCEL_CARD;
    }

    private ICustomEntity createParams(CancelCardDTO cancelCardDTO, long robotId) {
        String csrf = redisTemplate.opsForValue().get(createCacheKeyCsrf(robotId));
        return UrlCustomEntity
                .custom()
                // 1、提交表单的格式
                .add("submitFrom", cancelCardDTO.getSubmitFrom())
                // 卡类 1、话费卡 2、游戏卡 3、加油卡 4、电商卡
                .add("productClassifyId", cancelCardDTO.getProductClassifyId())
                // 卡种  1、中国移动快销   2、中国联通快销   3、中国电信快销   5.中国移动慢销   6.中国联通慢销   7、中国电信慢销  26、中国移动（超级快销）  27、中国联通（超级快销）
                .add("operatorId", cancelCardDTO.getOperatorId())
                .add("chooseOperatorId", cancelCardDTO.getChooseOperatorId())
                .add("price", cancelCardDTO.getPrice().toString()) // 面值
                // 折扣
                .add("discount", cancelCardDTO.getDiscount())
                // 提交方式：1、单卡提交   2、批量提交
                .add("type", cancelCardDTO.getType())
                .add("tempcards", cancelCardDTO.getTempcards())
                .add("cards", cancelCardDTO.getCards())
                .add("isAgree2", cancelCardDTO.getIsAgree2())
                .add("cardNumber", cancelCardDTO.getCardNumber())
                .add("cardPassword", cancelCardDTO.getCardPassword())
                .add("isAgree1", cancelCardDTO.getIsAgree1())
                .add("_csrf", csrf);
    }

    /**
     * 响应结果1：
     * {
     *     "code": "000009",
     *     "message": "该充值卡卡密正在处理中"
     * }
     */
    // 响应结果转换
    private static final class Parser implements IResultParse{
        private static final Parser INSTANCE = new Parser();
        private Parser(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            ResultVO doLockVO = JSON.parseObject(result, ResultVO.class);
            if (StringUtils.isEmpty(doLockVO.getCode())) {
                return ResponseResult.FAIL("未成功转换：请检查VO对象：ResultVO");
            }
            return Constant.SUCCESS.equals(doLockVO.getCode())
                    ? ResponseResult.SUCCESS() : ResponseResult.FAIL(doLockVO.getMessage());
        }
    }
}

