package com.robot.bbin.activity.interceptor;

import com.bbin.common.constant.CommonConstant;
import com.bbin.common.constant.TenantConstant;
import com.bbin.common.pojo.AuthToken;
import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.ResponseUtils;
import com.bbin.utils.project.XcTokenUtil;
import com.robot.center.constant.RobotConsts;
import com.robot.center.tenant.TContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class TenantInterceptor extends HandlerInterceptorAdapter {
    @Value("${spring.profiles.active}")
    private String environment;

    @Autowired
    private StringRedisTemplate redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (environment.equals(CommonConstant.DEV)) {
            TContext.setTenantId(6L);
            TContext.setChannelId(2L);
        } else {
            boolean setTenantId = set(request, TenantConstant.TENANT_ID);
            boolean setChannel = set(request, TenantConstant.CHANNEL_ID);
            if (!setTenantId || !setChannel) {
                boolean init = init(request, response);
                if (!init) {
                    return init;
                }
            }
        }
        TContext.setPlatformId(RobotConsts.PLATFORM_ID.BBIN);
        TContext.setFunction(RobotConsts.FUNCTION_CODE.ACTIVITY);
        // log4j2设置租户日志
        ThreadContext.put("TENANT_ID", TContext.getTenantId()+"");
        ThreadContext.put("CHANNEL_ID", TContext.getChannelId()+"");
        ThreadContext.put("PLATFORM_ID", RobotConsts.PLATFORM_ID.JIU_WU_CARD + "");
        ThreadContext.put("FUNCTION_CODE", RobotConsts.FUNCTION_CODE.ACTIVITY + "");
        return true;
    }

    private boolean set(HttpServletRequest request,String key){ //  如果是feign调用，从header里面取
        String value=request.getHeader(key);
        if (StringUtils.isEmpty(value)) return false;
        TContext.set(key, Long.parseLong(value));
        return true;
    }

    private boolean init(HttpServletRequest request,HttpServletResponse response) throws Exception{// 后台管理从session取
        AuthToken userInfo = null;
        try {
            userInfo = XcTokenUtil.getUserInfo(request, environment, redis);
        } catch (Exception e) {
            log.error("缓存获取管理员信息失败,请登录");
            ResponseUtils.writeJson(response, ResponseResult.FAIL("请重新登录"), HttpStatus.FORBIDDEN);
            return false;
        }
        if (null == userInfo || null == userInfo.getTenantId() || null == userInfo.getChannelId()) {
            ResponseUtils.writeJson(response, ResponseResult.FAIL("请重新登录"), HttpStatus.FORBIDDEN);
            return false;
        }
        TContext.set(TenantConstant.TENANT_ID,userInfo.getTenantId());
        TContext.set(TenantConstant.CHANNEL_ID,userInfo.getChannelId());
        TContext.set(TenantConstant.OPERATOR_ID,userInfo.getUserId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        TContext.clean(); //  清理线程
    }
}
