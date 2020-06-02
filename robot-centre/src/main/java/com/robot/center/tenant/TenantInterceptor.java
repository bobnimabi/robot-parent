package com.robot.center.tenant;

import com.bbin.common.constant.CommonConstant;
import com.bbin.common.constant.TenantConstant;
import com.bbin.common.pojo.AuthToken;
import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.ResponseUtils;
import com.bbin.utils.project.XcTokenUtil;
import com.robot.core.common.TContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public abstract class TenantInterceptor extends HandlerInterceptorAdapter {

    @Value("${spring.profiles.active}")
    private String environment;

    @Autowired
    private StringRedisTemplate redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (environment.equals(CommonConstant.DEV)) {
            this.setDevVariable();
        } else {
            String tenantId = this.getHeader(request, TenantConstant.TENANT_ID);
            String channelId = this.getHeader(request, TenantConstant.CHANNEL_ID);
            if (StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(channelId)) {
                return getBySession(request, response);
            } else {
                // log4j2设置租户日志
                TContext.setTenantId(tenantId);
                TContext.setChannelId(channelId);
            }
        }
        this.setPlatFormIdAndFunction();
        return true;
    }

    /**
     * 清除线程里携带的租户信息
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        TContext.clean();
    }

    /**
     * 设置DEV环境的租户
     */
    protected abstract void setDevVariable();

    /**
     * 设置Plaform和Function
     */
    protected abstract void setPlatFormIdAndFunction();

    /**
     * 从缓存里面获取租户信息（登录缓存）
     */
    private boolean getBySession(HttpServletRequest request, HttpServletResponse response) throws Exception{// 后台管理从session取
        AuthToken userInfo = null;
        try {
            userInfo = XcTokenUtil.getUserInfo(request, environment, redis);
        } catch (Exception e) {
            log.error("缓存获取管理员信息失败,请登录");
            ResponseUtils.writeJson(response, ResponseResult.FAIL("机器人：未获取到登录凭证，请登录"), HttpStatus.FORBIDDEN);
            return false;
        }
        if (null == userInfo || null == userInfo.getTenantId() || null == userInfo.getChannelId()) {
            ResponseUtils.writeJson(response, ResponseResult.FAIL("机器人：未获取到租户信息，请登录"), HttpStatus.FORBIDDEN);
            return false;
        }
        TContext.setTenantId(userInfo.getTenantId() + "");
        TContext.setChannelId(userInfo.getChannelId() + "");
        this.setPlatFormIdAndFunction();
        return true;
    }

    /**
     * 获取请求头
     */
    private String getHeader(HttpServletRequest request, String key){ //  如果是feign调用，从header里面取
        return request.getHeader(key);
    }
}
