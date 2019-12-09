package com.robot.center.interceptor;

import com.bbin.common.constant.CommonConstant;
import com.bbin.common.pojo.AuthToken;
import com.bbin.utils.project.RequestCheck;
import com.bbin.utils.project.XcCookieUtil;
import com.bbin.utils.project.XcTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class FlushTokenInterceptor extends HandlerInterceptorAdapter {
	@Value("${spring.profiles.active}")
	private String environment;
	@Autowired
	private StringRedisTemplate redis;

	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {
		if (environment.equals(CommonConstant.PROD) || environment.equals(CommonConstant.TEST)) {
			String uid = XcCookieUtil.getTokenFormCookie(request);
			AuthToken userToken = XcTokenUtil.getUserToken(uid, redis);
			// 刷新JWT令牌过期时间
			RequestCheck.flushJwtInDate(uid, redis);
			// 刷新登录标志
			RequestCheck.flushLoginFlag(userToken.getUsername(), redis);
			log.info("刷新cookie：uid：" + uid);
		}
		return true;
	}
}
