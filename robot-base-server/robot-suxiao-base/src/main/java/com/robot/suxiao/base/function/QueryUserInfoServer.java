package com.robot.suxiao.base.function;

import bom.bbin.common.dto.income.robot.WithDrawDTO;
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
import com.robot.suxiao.base.vo.BankInfoVO;
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
public class QueryUserInfoServer extends FunctionBase<WithDrawDTO> {

    @Autowired
    private StringRedisTemplate redis;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<WithDrawDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null, createParams(
                robotWrapper.getId()), null, Parser.INSTANCE);
        return standerHttpResponse.getResponseResult();
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.QUERY_USER_INFO;
    }

    private ICustomEntity createParams(long robotId) {
        String csrf = redis.opsForValue().get(createCacheKeyCsrf(robotId));
        return UrlCustomEntity
                .custom()
                .add("pf", "1")
                .add("_csrf", csrf);
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
            BankInfoVO doLockVO = JSON.parseObject(result, BankInfoVO.class);
            if (null == doLockVO || null == doLockVO.getBankCardList().get(0)
                    || StringUtils.isEmpty(doLockVO.getBankCardList().get(0).getId())) {
                return ResponseResult.FAIL("未成功转换：请检查VO对象：BankInfoVO");
            }
            return ResponseResult.SUCCESS(doLockVO);
        }
    }
}

