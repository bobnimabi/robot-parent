package com.robot.jiuwu.base.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.bbin.common.response.ResponseResult;
import com.rabbitmq.client.Channel;
import com.robot.center.constant.RobotConsts;
import com.robot.center.controller.RobotControllerBase;
import com.robot.center.dispatch.ITaskPool;
import com.robot.center.execute.TaskWrapper;
import com.robot.center.function.ParamWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.center.util.MoneyUtil;
import com.robot.jiuwu.base.basic.FunctionEnum;
import com.robot.jiuwu.base.dto.PayMoneyDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
public class JiuWuController extends RobotControllerBase {
    @Autowired
    private ITaskPool taskPool;

    @ApiOperation("机器人：获取图片验证码")
    @GetMapping("/getImageCode")
    public ResponseResult getImageCode( @RequestParam Long robotId) throws Exception {
        if (null == robotId) {
            return ResponseResult.FAIL("未传入robotId");
        }
        return distributeByRobot(null, FunctionEnum.Image_CODE_SERVER, robotId);
    }

    //查询用户是否存在
    @GetMapping("/isExist")
    public ResponseResult isExist(@RequestParam String username) throws Exception {
        ResponseResult result = distribute(new ParamWrapper<String>(username), FunctionEnum.QUERY_USER_SERVER);
        if (result.isSuccess()) {
            return ResponseResult.SUCCESS_MES("用户存在");
        }
        return result;
    }

    @ApiOperation("机器人：mq打款")
    @RabbitListener(queues = RabbitMqConstants.REMIT_QUEUE_95_CARD)
    @RabbitHandler
    public void payAmountMq(PayMoneyDTO payMoneyDTO, Channel channel, Message message) {
        // 如果tenant相关的设置失败则不进行ack
        // 如果是消息本身不具有tenant,只能人工进行删除
        if (!tenantDispatcher(RobotConsts.PLATFORM_ID.JIU_WU_CARD,RobotConsts.FUNCTION_CODE.ACTIVITY)) {
            return;
        }
        try {
            if (null == payMoneyDTO
                    || StringUtils.isEmpty(payMoneyDTO.getUsername())
                    || null == payMoneyDTO.getPaidAmount()
                    || StringUtils.isEmpty(payMoneyDTO.getMemo())
                    || StringUtils.isEmpty(payMoneyDTO.getOutPayNo())
            ) ResponseResult.FAIL("参数不全");
            log.info("mq打款入参：{}", JSON.toJSONString(payMoneyDTO));

            payMoneyDTO.setPaidAmount(MoneyUtil.formatYuan(payMoneyDTO.getPaidAmount()));
            payMoneyDTO.setUsername(payMoneyDTO.getUsername().trim());
            TaskWrapper taskWrapper = new TaskWrapper(new ParamWrapper<PayMoneyDTO>(payMoneyDTO), FunctionEnum.PAY_SERVER, payMoneyDTO.getUsername(), Duration.ofSeconds(12));

            String externalNo = payMoneyDTO.getOutPayNo();
            if (StringUtils.isNotBlank(externalNo)) {
                boolean isRedo = isRedo(externalNo);
                if (isRedo) {
                    log.info("该外部订单号已经存在,将不执行,externalNo:{},功能参数:{}", externalNo, JSON.toJSONString(taskWrapper));
                    return;
                }
            }
            taskPool.taskAdd(taskWrapper,payMoneyDTO.getOutPayNo());
        } catch (Exception e) {
            log.info("机器人：MQ打款异常", e);
        }finally {
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            } catch (IOException e) {
                log.error("teantId:{},channelId:{},mq的Ack异常", RobotThreadLocalUtils.getTenantId(), RobotThreadLocalUtils.getChannelId(), e);
            }
            RobotThreadLocalUtils.clean();
        }
    }
}
