package com.robot.jiuwu.base.function;

import com.robot.center.constant.RobotConsts;
import com.robot.center.mq.MqSenter;
import com.robot.code.response.Response;
import com.robot.core.function.base.*;
import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.ao.PayAO;
import com.robot.jiuwu.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 付款
 */
@Slf4j
@Service
public class JiuWuAbstractFunction extends AbstractFunction<Object, String, Object> {

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private LoginFunction loginFunction;

    @Override
    protected IPathEnum getPathEnum() {
        return null;
    }

    /**
     * 获取接口特定请求头
     * 注意：可以对公共头进行覆盖（tenant_robot_header表配置）
     * 有些特定的登录的token会存在于cookie的属性里面
     * @param robotWrapper
     * @return
     */
    protected CustomHeaders getHeaders(RobotWrapper robotWrapper) {


        return    CustomHeaders.custom(1)
                    .add("header",getTokenMethod(robotWrapper));

    }

    @Override
    protected IEntity getEntity(Object params, RobotWrapper robotWrapper) {
        return null;
    }


    @Override
    protected IResultHandler<String, Object> getResultHandler() {
        return null;
    }


    public String getTokenMethod( RobotWrapper robotWrapper){

        Long robotId = robotWrapper.getId();
        String cacheKeyLoginToken = loginFunction.createCacheKeyLoginToken(robotId);
        String token = redis.opsForValue().get(cacheKeyLoginToken);
        if (!StringUtils.isEmpty(token)) {
            Boolean expire = redis.expire(cacheKeyLoginToken, 1, TimeUnit.DAYS);
            if (!expire) {
                log.info("刷新九五棋牌Token失败,RobotId:{}", robotId);
            }
    }
        return token;
}

}
