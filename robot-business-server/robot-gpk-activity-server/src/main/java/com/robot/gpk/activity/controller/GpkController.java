package com.robot.gpk.activity.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.pojo.TaskAtomDto;
import com.bbin.common.response.ResponseResult;
import com.rabbitmq.client.Channel;
import com.robot.center.constant.RobotConsts;
import com.robot.center.controller.ControllerBase;
import com.robot.code.dto.LoginDTO;
import com.robot.code.response.Response;
import com.robot.core.common.TContext;
import com.robot.core.function.base.ParamWrapper;
import com.robot.gpk.base.basic.FunctionEnum;
import com.robot.gpk.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
@RestController
public class GpkController extends ControllerBase {
    /**
     * 机器人登录
     * 注意：登录的第一个接口isNewCookie：true
     */
    @PostMapping("/robotLogin")
    public Response robotLogin(@RequestBody LoginDTO loginDTO) throws Exception {
        if (null == loginDTO || null == loginDTO.getId()) {
            return Response.FAIL("未传入参数");
        }
        Response response = super.dispatcher.disPatcherLogin(new ParamWrapper(loginDTO), FunctionEnum.LOGIN_SERVER, true);
        if (!response.isSuccess()) {
            return response;
        }
        return Response.SUCCESS("登录成功");
    }

    /**
     * 查询用户是否存在
     */
    @GetMapping("/isExist")
    public Response isExist(@RequestParam String username) throws Exception {
        return super.dispatcher.dispatch(new ParamWrapper<String>(username), FunctionEnum.QUERY_USER_SERVER);
    }




    /**
     * 幸运注单
     * 1.会员在【PT电子、SG电子、RT电子、JDB电子、CQ9】中进行投注
     * 2.此活动仅限老虎机与经典老虎机游戏中产生的注单
     */
    @PostMapping("/luckyNo")
    public Response queryOrderNo(@RequestBody OrderNoQueryDTO orderNoQueryDTO) throws Exception{
        if (null == orderNoQueryDTO
                || StringUtils.isEmpty(orderNoQueryDTO.getOrderNo())
                || StringUtils.isEmpty(orderNoQueryDTO.getGameCode()) // 平台编码
        ) {
            return Response.FAIL("参数不全");
        }
        return super.dispatcher.dispatch(new ParamWrapper<OrderNoQueryDTO>(orderNoQueryDTO), FunctionEnum.ORDER_QUERY_SERVER);
    }


    /**
     * 机器人mq打款
     */
    @RabbitListener(queues = RabbitMqConstants.REMIT_QUEUE_GPK)
    @RabbitHandler
    public void payAmountMq(TaskAtomDto taskAtomDto, Channel channel, Message message) {
        try {
            super.handleTenant(RobotConsts.PLATFORM_ID.GPK,RobotConsts.FUNCTION_CODE.ACTIVITY);
            log.info("mq打款入参：{}", JSON.toJSONString(taskAtomDto));
            if (null == taskAtomDto
                    || StringUtils.isEmpty(taskAtomDto.getUsername())
                    || null == taskAtomDto.getPaidAmount()
                    || StringUtils.isEmpty(taskAtomDto.getMemo())
                    || StringUtils.isEmpty(taskAtomDto.getOutPayNo())
            ) {
                ResponseResult.FAIL("参数不全");
            }
            if (taskAtomDto.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
                log.error("金额不能小于等于0,paidAmount:"+taskAtomDto.getPaidAmount());
                return;
            }
            taskAtomDto.setUsername(taskAtomDto.getUsername().trim());
            super.dispatcher.asyncDispatch(new ParamWrapper(taskAtomDto),taskAtomDto.getOutPayNo(), PathEnum.PAY,FunctionEnum.PAY_SERVER);
        } catch (Exception e) {
            log.info("机器人：MQ打款异常", e);
        }finally {
            if (null != channel) {
                try {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                } catch (IOException e) {
                    log.error("teantId:{},channelId:{},mq的Ack异常", TContext.getTenantId(), TContext.getChannelId(), e);
                }
            }
            TContext.clean();
        }
    }

    /**
     * 测试打款接口使用，生产环境不用
     * @param taskAtomDto
     * @return
     * @throws Exception
     */
    @PostMapping("/tempPay")
    public void tempPay(@RequestBody TaskAtomDto taskAtomDto) throws Exception {
        log.info("mq打款入参：{}", JSON.toJSONString(taskAtomDto));
        if (null == taskAtomDto
                || StringUtils.isEmpty(taskAtomDto.getUsername())
                || null == taskAtomDto.getPaidAmount()
                || StringUtils.isEmpty(taskAtomDto.getMemo())
                || StringUtils.isEmpty(taskAtomDto.getOutPayNo())
        ) {
            ResponseResult.FAIL("参数不全");
        }
        if (taskAtomDto.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("金额不能小于等于0,paidAmount:"+taskAtomDto.getPaidAmount());
            return;
        }
        taskAtomDto.setUsername(taskAtomDto.getUsername().trim());
        super.dispatcher.asyncDispatch(new ParamWrapper(taskAtomDto),taskAtomDto.getOutPayNo(), PathEnum.PAY,FunctionEnum.PAY_SERVER);
    }
}
