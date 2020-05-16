package com.robot.gpk.base.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.pojo.TaskAtomDto;
import com.bbin.common.response.ResponseResult;
import com.rabbitmq.client.Channel;
import com.robot.center.constant.RobotConsts;
import com.robot.center.controller.RobotControllerBase;
import com.robot.center.dispatch.ITaskPool;
import com.robot.center.execute.TaskWrapper;
import com.robot.center.function.ParamWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.center.util.MoneyUtil;
import com.robot.gpk.base.basic.FunctionEnum;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.time.Duration;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
public class GpkController extends RobotControllerBase {

    @Autowired
    private ITaskPool taskPool;

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
    @RabbitListener(queues = RabbitMqConstants.REMIT_QUEUE_GPK)
    @RabbitHandler
    public void payAmountMq(TaskAtomDto taskAtomDto, Channel channel, Message message) {
        // 如果tenant相关的设置失败则不进行ack
        // 如果是消息本身不具有tenant,只能人工进行删除
        if (!tenantDispatcher(RobotConsts.PLATFORM_ID.GPK,RobotConsts.FUNCTION_CODE.ACTIVITY)) {
            return;
        }
        try {
            if (null == taskAtomDto
                    || StringUtils.isEmpty(taskAtomDto.getUsername())
                    || null == taskAtomDto.getPaidAmount()
                    || StringUtils.isEmpty(taskAtomDto.getMemo())
                    || StringUtils.isEmpty(taskAtomDto.getOutPayNo())
            ) ResponseResult.FAIL("参数不全");
            log.info("mq打款入参：{}", JSON.toJSONString(taskAtomDto));

            taskAtomDto.setPaidAmount(MoneyUtil.formatYuan(taskAtomDto.getPaidAmount()));
            taskAtomDto.setUsername(taskAtomDto.getUsername().trim());
            TaskWrapper taskWrapper = new TaskWrapper(new ParamWrapper<TaskAtomDto>(taskAtomDto), FunctionEnum.PAY_SERVER, taskAtomDto.getUsername(), Duration.ofSeconds(12));

            String externalNo = taskAtomDto.getOutPayNo();
            if (StringUtils.isNotBlank(externalNo)) {
                boolean isRedo = isRedo(externalNo);
                if (isRedo) {
                    log.info("该外部订单号已经存在,将不执行,externalNo:{},功能参数:{}", externalNo, JSON.toJSONString(taskWrapper));
                    return;
                }
            }
            taskPool.taskAdd(taskWrapper,taskAtomDto.getOutPayNo());
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
