package com.robot.jiuwu.pay.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.response.ResponseResult;
import com.rabbitmq.client.Channel;
import com.robot.center.function.ParamWrapper;
import com.robot.jiuwu.base.dto.UpdateRemark2DTO;
import com.robot.jiuwu.base.dto.UpdateWithdrawStatusDTO;
import com.robot.jiuwu.base.function.UpdateRemark2Server;
import com.robot.jiuwu.base.function.UpdateWithdrawStatusServer;
import com.robot.jiuwu.pay.common.JiuWuPayConsts;
import com.robot.jiuwu.pay.function.WithdrawServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by mrt on 2020/1/11 0011 14:02
 */
@Slf4j
@RestController
public class PayController {
    @Autowired
    private UpdateRemark2Server remark2Server;
    @Autowired
    private UpdateWithdrawStatusServer withdrawStatusServer;
    @Autowired
    private WithdrawServer withdrawServer;

    /**
     * 出款回调
     */
    @RabbitListener(queues = RabbitMqConstants.PAY_AGENT_RETURN_OG_QUEUE)
    @RabbitHandler
    public void receiveMessageMq(ResponseResult responseResult, Channel channel, Message message)  {
        log.info("机器人打款回调 返回数据:[{}]", JSON.toJSONString(responseResult));
        LinkedHashMap obj = (LinkedHashMap)responseResult.getObj();
        // 会员账号
        String platformUserName = (String) obj.get("platformUserName");
        // 携带数据，此时用作订单号
        String platUserId = (String) obj.get("platUserId");
        long orderId = Long.parseLong(platUserId);
        try {
            // 成功：点击“确定”按钮
            if (responseResult.isSuccess()) {
                withdrawStatusServer.doFunction(new ParamWrapper<UpdateWithdrawStatusDTO>(new UpdateWithdrawStatusDTO(platUserId, JiuWuPayConsts.WITHDRAW_SUCCESS)));
            }else{// 失败：添加失败“备注”
                remark2Server.doFunction(new ParamWrapper<UpdateRemark2DTO>(new UpdateRemark2DTO(orderId, responseResult.getMessage())));
            }
        } catch (Exception e) {
            log.info("线下打款设置状态异常", e);
        }
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            log.info("线下打款回调ACK异常", e);
        }
    }
}
