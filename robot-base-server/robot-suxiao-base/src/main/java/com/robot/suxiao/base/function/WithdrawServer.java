package com.robot.suxiao.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.http.CustomHttpMethod;
import com.robot.center.http.ICustomEntity;
import com.robot.center.http.StanderHttpResponse;
import com.robot.center.http.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.suxiao.base.basic.ActionEnum;
import com.robot.suxiao.base.common.Constant;
import com.robot.suxiao.base.vo.BankInfoVO;
import com.robot.suxiao.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import static com.robot.suxiao.base.function.MainServer.createCacheKeyCsrf;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询银行卡结果
 */
@Slf4j
@Service
public class WithdrawServer extends FunctionBase<BankInfoVO> {

    @Autowired
    private StringRedisTemplate redis;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<BankInfoVO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        BankInfoVO bankInfoVO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null, createParams(
                robotWrapper, bankInfoVO), null, Parser.INSTANCE);
        return standerHttpResponse.getResponseResult();
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.WITHDRAW;
    }

    private ICustomEntity createParams(RobotWrapper robotWrapper, BankInfoVO bankInfoVO) {
        String csrf = redis.opsForValue().get(createCacheKeyCsrf(robotWrapper.getId()));
        return UrlCustomEntity
                .custom()
                .add("_csrf", csrf)
                .add("platform", "1")
                .add("type", "3") // 1微信钱包 3银行卡 4支付宝
                .add("bankCardId", bankInfoVO.getBankCardList().get(0).getId()) // 银行卡ID
                .add("money", bankInfoVO.getM().getMoney())
                .add("tradePassword", robotWrapper.getPlatformPassword()); // 平台密码和提款密码一致
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
            ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
            if (StringUtils.isEmpty(resultVO.getCode())) {
                return ResponseResult.FAIL("未成功转换：请检查VO对象：ResultVO");
            }
            return Constant.SUCCESS.equals(resultVO.getCode())
                    ? ResponseResult.SUCCESS() : ResponseResult.FAIL(resultVO.getMessage());
        }
    }
}

