package com.robot.suxiao.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.constant.RobotConsts;
import com.robot.center.execute.CommonActionEnum;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.ICustomEntity;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.suxiao.base.basic.ActionEnum;
import com.robot.suxiao.base.common.Constant;
import com.robot.suxiao.base.vo.MainVO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.UUID;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录预备请求：获取登录参数
 */
@Slf4j
@Service
public class MainServer extends FunctionBase<LoginDTO> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.GET, action, null, null, null, LoginParse.INSTANCE);
        ResponseResult result = standerHttpResponse.getResponseResult();
        MainVO mainVO = (MainVO) result.getObj();
        redisTemplate.opsForValue().set(createCacheKeyCsrf(robotWrapper.getId()), mainVO.getCsrf());
        return result;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.MAIN;
    }


    // 响应结果转换
    private static final class LoginParse implements IResultParse{
        private static final LoginParse INSTANCE = new LoginParse();
        private LoginParse(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            Document doc = Jsoup.parse(result);
            Element csrf = doc.getElementById("csrf");
            Element type = doc.getElementById("type");
            Element loginType = doc.getElementById("loginType");
            Element platform = doc.getElementById("platform");
            Element error = doc.getElementById("error");
            MainVO mainVO = new MainVO(csrf.val(), type.val(), loginType.val(), platform.val(), error.val());
            return ResponseResult.SUCCESS(mainVO);
        }
    }
    // 创建缓存id的缓存标志
    static String createCacheKeyCsrf(long robotId) {
        return new StringBuilder(100)
                .append(RobotConsts.CAPTCHA_TOKEN)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction()).append(":")
                .append(robotId)
                .toString();
    }
}
