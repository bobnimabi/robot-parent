package com.robot.jiuwu.activity.interceptor;

import com.robot.center.constant.RobotConsts;
import com.robot.jiuwu.base.function.LoginFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *注意：95棋牌的所有请求都需要带上token（token来自于登录响应）
 * </p>
 *
 * @author tank  HandlerInterceptorAdapter
 * @date 2020/7/7
 *
 */
@Slf4j
@Service
public class JiuWutokenInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private StringRedisTemplate redis;
	@Autowired
	private LoginFunction loginServer;

	/*//@Override
	protected List<HttpRequestInterceptor> getRequestInterceptor() {
		List<HttpRequestInterceptor> list = new ArrayList<>(1);
		list.add( new TokenInterceptor());
		return list;
	}*/
	/**
	 * Token拦截器
	 */
	private class TokenInterceptor implements HttpRequestInterceptor{
		@Override
		public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
			Long robotId = (Long)httpContext.getAttribute(RobotConsts.ROBOT_ID);
			String cacheKeyLoginToken = loginServer.createCacheKeyLoginToken(robotId);
			String token = redis.opsForValue().get(cacheKeyLoginToken);
			if (!StringUtils.isEmpty(token)) {
				Boolean expire = redis.expire(cacheKeyLoginToken, 1, TimeUnit.DAYS);
				if (!expire) {
					log.info("刷新九五棋牌Token失败,RobotId:{}", robotId);
				}
			}
			httpRequest.addHeader("token",token);
		}
	}
}
