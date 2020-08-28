package com.robot.center.interceptor;

import com.bbin.common.constant.CommonConstant;
import com.bbin.common.pojo.AuthToken;
import com.bbin.utils.project.RequestUtils;
import com.bbin.utils.project.XcTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginLogInterceptor extends HandlerInterceptorAdapter {

	@Value("${spring.profiles.active}")
	private String environment;

	@Autowired
	private StringRedisTemplate redis;

	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {
		//记录用户行为
		AuthToken userInfo = XcTokenUtil.getUserInfo(request, environment, redis);
		log.info("IP:" + RequestUtils.getIpAddress(request) + " 管理员:" + userInfo.getUsername() + " 动作：" + request.getRequestURI());
		return true;
	}


}
