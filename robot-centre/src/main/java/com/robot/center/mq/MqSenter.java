package com.robot.center.mq;

import com.bbin.common.response.ResponseResult;
import com.bbin.common.util.ThreadLocalUtils;
import com.robot.center.tenant.TContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 11/22/2019 1:02 PM
 */
@Slf4j
@Service
public class MqSenter {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, String route, ResponseResult responseResult) {
        ThreadLocalUtils.setTenantId(TContext.getTenantId());
        ThreadLocalUtils.setChannelId(TContext.getChannelId());
        try {
            rabbitTemplate.convertAndSend(exchange, route, responseResult);
        }finally {
            ThreadLocalUtils.clean();
        }
    }
}
