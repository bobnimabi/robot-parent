package com.robot.gpk.activity.interceptor;

import com.bbin.common.constant.CommonConstant;
import com.bbin.common.constant.TenantConstant;
import com.bbin.common.pojo.AuthToken;
import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.ResponseUtils;
import com.bbin.utils.project.XcTokenUtil;
import com.robot.center.constant.RobotConsts;
import com.robot.center.tenant.RobotThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
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
@Service
public class TenantInterceptor extends HandlerInterceptorAdapter {

    @Value("${spring.profiles.active}")
    private String environment;

    @Autowired
    private StringRedisTemplate redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (environment.equals(CommonConstant.DEV)) {
            RobotThreadLocalUtils.setTenantId(5L);
            RobotThreadLocalUtils.setChannelId(5L);
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
        RobotThreadLocalUtils.setPlatformId(RobotConsts.PLATFORM_ID.GPK);
        RobotThreadLocalUtils.setFunction(RobotConsts.FUNCTION_CODE.ACTIVITY);
        // log4j2设置租户日志
        ThreadContext.put("TENANT_ID",RobotThreadLocalUtils.getTenantId()+"");
        ThreadContext.put("CHANNEL_ID",RobotThreadLocalUtils.getChannelId()+"");
        ThreadContext.put("PLATFORM_ID", RobotConsts.PLATFORM_ID.GPK + "");
        ThreadContext.put("FUNCTION_CODE", RobotConsts.FUNCTION_CODE.ACTIVITY + "");
        return true;
    }

    private boolean set(HttpServletRequest request,String key){ //  如果是feign调用，从header里面取
        String value=request.getHeader(key);
        if (StringUtils.isEmpty(value)) return false;
        RobotThreadLocalUtils.set(key, Long.parseLong(value));
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
        RobotThreadLocalUtils.set(TenantConstant.TENANT_ID,userInfo.getTenantId());
        RobotThreadLocalUtils.set(TenantConstant.CHANNEL_ID,userInfo.getChannelId());
        RobotThreadLocalUtils.set(TenantConstant.OPERATOR_ID,userInfo.getUserId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        RobotThreadLocalUtils.clean(); //  清理线程
        ThreadContext.clearMap();
    }
}
