package com.robot.center.mq;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.pojo.PayResponseVo;
import com.bbin.common.response.CommonCode;
import com.bbin.common.response.ResponseResult;
import com.bbin.common.util.ThreadLocalUtils;
import com.robot.core.common.TContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/22/2019 1:02 PM
 */
@Slf4j
@Service
public class MqSenter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private void sendMessage(String exchange, String route, ResponseResult responseResult) {
        ThreadLocalUtils.setTenantId(TContext.getTenantId());
        ThreadLocalUtils.setChannelId(TContext.getChannelId());
        try {
            rabbitTemplate.convertAndSend(exchange, route, responseResult);
        }finally {
            ThreadLocalUtils.clean();
        }
    }

    // 组装响应对象并发布
    public void topicPublic(String robotRecordId, String outPayNo, boolean isSuccess, String errorMes, String theme, BigDecimal paidAmount) {
        //构建响应信息
        PayResponseVO payResponseVo = new PayResponseVO(robotRecordId,outPayNo,paidAmount);
        //使用消息队列通知其他微服务
        ResponseResult resp = null;
        if (!isSuccess) {
            resp = ResponseResult.FAIL_OBJ(errorMes, JSON.toJSONString(payResponseVo));
        } else {
            resp = new ResponseResult(CommonCode.PAY_SUCCESS, JSON.toJSONString(payResponseVo));
        }
        this.sendMessage(RabbitMqConstants.ROBOT_SUCCESS_EXCHANGE_NAME, RabbitMqConstants.ROBOT_SUCCESS_ROUTE_KEY, resp);
    }
}
