package com.robot.suxiao.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.constant.RobotConsts;
import com.robot.center.execute.CommonActionEnum;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.*;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.suxiao.base.common.Constant;
import com.robot.suxiao.base.dto.XiaoLoginDTO;
import com.robot.suxiao.base.vo.MainVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import java.time.Duration;
import java.util.UUID;

import static com.robot.suxiao.base.function.MainServer.createCacheKeyCsrf;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 人人销卡登录
 */
@Slf4j
@Service
public class LoginServer extends FunctionBase<LoginDTO> {

    @Autowired
    private MainServer mainServer;

    @Autowired
    private StringRedisTemplate redis;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        ResponseResult mainResult = mainServer.doFunction(paramWrapper, robotWrapper);
        if (!mainResult.isSuccess()) {
            return mainResult;
        }
        MainVO mainVO = (MainVO) mainResult.getObj();

        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null, createLoginParams(robotWrapper, mainVO), null, LoginParse.INSTANCE, false);
        if (HttpStatus.SC_MOVED_TEMPORARILY == standerHttpResponse.getStatusLine().getStatusCode()) {
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL("登录失败");
    }

    @Override
    public IActionEnum getActionEnum() {
        return CommonActionEnum.LOGIN;
    }

    // 组装登录参数
    private ICustomEntity createLoginParams(RobotWrapper robot, MainVO mainVO) {
        String csrf = redis.opsForValue().get(createCacheKeyCsrf(robot.getId()));
        return UrlCustomEntity.custom()
                .add("username", robot.getPlatformAccount())
                .add("password", robot.getPlatformPassword())
                .add("phone", "")
                .add("remember_me", "on")
                .add("_csrf", csrf)
                .add("type", mainVO.getType())
                .add("loginType", mainVO.getLoginType())
                .add("platform", mainVO.getPlatform())
                .add("error", mainVO.getError());
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
            String title = doc.getElementsByTag("title").get(0).val();
            Element error = doc.getElementById("error");
            if (null != error && !StringUtils.isEmpty(error.val())) {
                return ResponseResult.FAIL("登录失败：" + error.val());
            }
            return ResponseResult.SUCCESS("登录成功");
        }
    }

    // 创建机器人的登录TOKEN
    public static String createCacheKeyLoginToken(long robotId) {
        return new StringBuilder(50)
                .append(RobotConsts.LOGIN_TOKEN)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction()).append(":")
                .append(robotId)
                .toString();
    }
}
