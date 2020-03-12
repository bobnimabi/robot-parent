package com.robot.suxiao.base.function;

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
import com.robot.center.util.CookieUtil;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobotAction;
import com.robot.suxiao.base.vo.MainVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    private static final String DOMAIN = "renrenxiaoka.com";

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<LoginDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        ResponseResult mainResult = mainServer.doFunction(paramWrapper, robotWrapper);
        if (!mainResult.isSuccess()) {
            return mainResult;
        }
        MainVO mainVO = (MainVO) mainResult.getObj();
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null, createLoginParams(robotWrapper, mainVO), null, LoginParse.INSTANCE, false);
        ResponseResult responseResult = standerHttpResponse.getResponseResult();
        if (responseResult.isSuccess()) {
            // 修改下cookie
            addCookie(robotWrapper);
        }
        return responseResult;
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

    /**
     * 将cookie：b_sessionid的新天加一个域名api.renrenxiaoka.com（原始的为：renrenxiaoka.com）
      */
    public void addCookie(RobotWrapper robotWrapper) {
        CookieStore cookieStore = robotWrapper.getCookieStore();
        for (Cookie cookie : cookieStore.getCookies()) {
            if (DOMAIN.equalsIgnoreCase(cookie.getDomain())) {
                BasicClientCookie addCookie = CookieUtil.createCookie("api.renrenxiaoka.com", cookie.getName(), cookie.getValue(),
                        cookie.getPath(), cookie.getExpiryDate(), cookie.isSecure(), cookie.getVersion());
                cookieStore.addCookie(addCookie);
            }
        }
    }

    // 响应结果转换
    private static final class LoginParse implements IResultParse {
        private static final LoginParse INSTANCE = new LoginParse();

        private LoginParse() {
        }

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

    /**
     * 创建机器人的登录TOKEN
     * @param robotId
     * @return
     */
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
