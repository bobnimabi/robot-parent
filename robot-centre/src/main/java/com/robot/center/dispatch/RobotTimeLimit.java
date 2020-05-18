package com.robot.center.dispatch;

import com.robot.center.constant.RobotConsts;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.center.util.NetWorkUtil;
import com.robot.code.entity.TenantRobotAction;
import com.robot.code.entity.TenantRobotProxy;
import com.robot.code.service.ITenantRobotProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 11/1/2019 12:22 PM
 */
@Slf4j
@Service
public class RobotTimeLimit {

    private static final String CACHE_ROBOT_IP_LIMIT = RobotConsts.ROBOT_PROJECT_PERFIX + "DISPATCH:IP_LIMIT:";
    private static final String CACHE_ROBOT_ME_LIMIT = RobotConsts.ROBOT_PROJECT_PERFIX + "DISPATCH:ME_LIMIT:";

    @Autowired
    private ITenantRobotProxyService proxyService;

    @Autowired
    private StringRedisTemplate redis;


    public boolean isExecute(TenantRobotAction action, RobotWrapper robotWrapper) {
        // 检查ip限制
        String ipLimitKey = createCacheIPLimitKey(robotWrapper.getId());
        if (0 != action.getIpTimeLimit()) {
            Boolean setIp = redis.opsForValue().setIfAbsent(ipLimitKey, "", action.getIpTimeLimit(), TimeUnit.SECONDS);
            if (!setIp) {
                log.info("IP时间限制，暂不执行");
                return false;
            }
        }
        // 检查机器人Action限制
        String robotLimitKey = createCacheRobotLimitKey(action.getActionCode(), robotWrapper.getId());
        if (0 != action.getRobotTimeLimit()) {
            Boolean setKey = redis.opsForValue().setIfAbsent(robotLimitKey, "", action.getRobotTimeLimit(), TimeUnit.SECONDS);
            if (!setKey) {
                log.info("ROBOT时间限制，暂不执行");
                return false;
            }
        }
        return true;
    }
    // CACHE：创建机器人执行Field限制时间key
    private static String createCacheRobotLimitKey(String actionCode, long robotId) {
        return new StringBuilder(45)
                .append(CACHE_ROBOT_ME_LIMIT)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction()).append(":")
                .append(robotId).append(":")
                .append(actionCode)
                .toString();
    }

    // CACHE：创建机器人执行IP限制时间key
    private String createCacheIPLimitKey(long robotId) {
        TenantRobotProxy proxy = proxyService.getProxy(robotId);
        String ip = NetWorkUtil.getLocalIpAddress();
        if (null != proxy) {
            ip = proxy.getProxyIp();
        }
        return new StringBuilder(45)
                .append(CACHE_ROBOT_IP_LIMIT)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction()).append(":")
                .append(ip) // 本机IP，如果使用代理，则是代理IP
                .toString();
    }
}
