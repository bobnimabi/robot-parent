package com.robot.bbin.activity.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.pojo.TaskAtomDto;
import com.bbin.common.response.ResponseResult;
import com.bbin.common.util.ThreadLocalUtils;
import com.bbin.utils.project.MyBeanUtil;
import com.rabbitmq.client.Channel;
import com.robot.bbin.activity.basic.FunctionEnum;
import com.robot.bbin.activity.dto.GameChild;
import com.robot.bbin.activity.dto.OrderNoQueryDTO;
import com.robot.bbin.activity.dto.PayMoneyDTO;
import com.robot.center.controller.RobotControllerBase;
import com.robot.center.dispatch.ITaskPool;
import com.robot.center.execute.TaskWrapper;
import com.robot.center.function.ParamWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.center.util.MoneyUtil;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
@RestController
public class BbinActivityController extends RobotControllerBase {
    @Autowired
    private ITaskPool taskPool;
    private BigDecimal AMOUNT_LIMIT = new BigDecimal(2000);


    //查询用户是否存在
    @GetMapping("/isExist")
    public ResponseResult isExist(@RequestParam String username) throws Exception {
        ResponseResult result = distribute(new ParamWrapper<String>(username), FunctionEnum.QUERY_BALANCE_SERVER);
        if (result.isSuccess()) {
            return ResponseResult.SUCCESS_MES("用户存在");
        }
        return result;
    }

    /**
     * 幸运注单
     * 1.会员在【PT电子、SG电子、RT电子、JDB电子、CQ9】中进行投注
     * 2.此活动仅限老虎机与经典老虎机游戏中产生的注单
     */
    @PostMapping("/queryOrderNo")
    public ResponseResult queryOrderNo(@RequestBody OrderNoQueryDTO orderNoQueryDTO) throws Exception{
        if (null == orderNoQueryDTO
                || StringUtils.isEmpty(orderNoQueryDTO.getOrderNo())
                || StringUtils.isEmpty(orderNoQueryDTO.getGameCode())//平台编码
        ) return ResponseResult.FAIL("参数不全");
        return distribute(new ParamWrapper<OrderNoQueryDTO>(orderNoQueryDTO), FunctionEnum.LUCK_ORDER_SERVER);
    }

    /**
     * 消消乐查询OG专用
     * @param queryDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/getEliminateToLe")
    public ResponseResult getEliminateToLe(@RequestBody OrderNoQueryDTO queryDTO) throws Exception {
        if (null == queryDTO
                || StringUtils.isEmpty(queryDTO.getOrderNo())
                || null == queryDTO.getStartDate()
                || null == queryDTO.getEndDate()
                || StringUtils.isEmpty(queryDTO.getGameCode())//平台编码
        ) return ResponseResult.FAIL("参数不全");
        queryDTO.setIs_BBIN(false);
        queryDTO.setGameCode("5");
        List<GameChild> list = new ArrayList<>();
        list.add(new GameChild(null, "5902", "糖果派对"));
        list.add(new GameChild(null, "5908", "糖果派对2"));
        list.add(new GameChild(null, "5143", "糖果派对3"));
        list.add(new GameChild(null, "5901", "连环夺宝"));
        list.add(new GameChild(null, "5912", "连环夺宝2"));
        list.add(new GameChild(null, "5911", "宝石派对"));
        list.add(new GameChild(null, "5907", "趣味台球"));
        queryDTO.setChildren(list);
        return distribute(new ParamWrapper<OrderNoQueryDTO>(queryDTO), FunctionEnum.BREAK_SERVER);
    }


    /**
     * 消消乐查询BBIN专用
     * @param queryDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/getEliminateToLe2")
    public ResponseResult getEliminateToLe2(@RequestBody OrderNoQueryDTO queryDTO) throws Exception {
        if (null == queryDTO
                || StringUtils.isEmpty(queryDTO.getOrderNo())
                || null == queryDTO.getStartDate()
                || null == queryDTO.getEndDate()
                || StringUtils.isEmpty(queryDTO.getGameCode())//平台编码
        ) return ResponseResult.FAIL("参数不全");
        queryDTO.setIs_BBIN(true);
        return distribute(new ParamWrapper<OrderNoQueryDTO>(queryDTO), FunctionEnum.BREAK_SERVER);
    }

    @ApiOperation("机器人：mq打款")
    @RabbitListener(queues = RabbitMqConstants.REMIT_QUEUE_BBIN)
    @RabbitHandler
    public void payAmountMq(PayMoneyDTO payMoneyDTO, Channel channel, Message message) {
        try {
            tenantDispatcher();
        } catch (Exception e) {
            log.info("未获取到tenant,");
        }
        try {
            if (null == payMoneyDTO
                    || StringUtils.isEmpty(payMoneyDTO.getUsername())
                    || null == payMoneyDTO.getPaidAmount()
                    || StringUtils.isEmpty(payMoneyDTO.getMemo())
                    || StringUtils.isEmpty(payMoneyDTO.getOutPayNo())
            ) ResponseResult.FAIL("参数不全");
            log.info("mq打款入参：{}", JSON.toJSONString(payMoneyDTO));

            if (payMoneyDTO.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
                ResponseResult.FAIL("金额不能小于等于0");
            }

            if (payMoneyDTO.getPaidAmount().compareTo(AMOUNT_LIMIT) > 0) {
                ResponseResult.FAIL("打款金额不能超过：" + AMOUNT_LIMIT.toString() + "元");
            }
            payMoneyDTO.setPaidAmount(MoneyUtil.formatYuan(payMoneyDTO.getPaidAmount()));
            payMoneyDTO.setUsername(payMoneyDTO.getUsername().trim());
            TaskWrapper taskWrapper = new TaskWrapper(new ParamWrapper<PayMoneyDTO>(payMoneyDTO), FunctionEnum.PAY_SERVER, payMoneyDTO.getUsername(), Duration.ofSeconds(12));
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
