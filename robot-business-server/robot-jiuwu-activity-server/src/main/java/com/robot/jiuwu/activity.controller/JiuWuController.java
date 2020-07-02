package com.robot.jiuwu.activity.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.pojo.TaskAtomDto;
import com.bbin.common.response.ResponseResult;
import com.rabbitmq.client.Channel;
import com.robot.center.constant.RobotConsts;
import com.robot.center.controller.ControllerBase;
//import com.robot.center.dispatch.ITaskPool;
//import com.robot.center.execute.TaskWrapper;
//import com.robot.center.function.ParamWrapper;
//import com.robot.center.tenant.RobotThreadLocalUtils;
//import com.robot.jiuwu.base.basic.FunctionEnum;
//import io.swagger.annotations.ApiOperation;
import com.robot.center.util.MoneyUtil;
import com.robot.code.dto.LoginDTO;
import com.robot.code.response.Response;
import com.robot.core.common.TContext;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.task.dispatcher.TaskWrapper;
import com.robot.jiuwu.base.basic.FunctionEnum;
import com.robot.jiuwu.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
public class JiuWuController extends ControllerBase {
/*    @Autowired
    private ITaskPool taskPool;*/

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


    //查询用户是否存在
    @GetMapping("/isExist")
    public Response isExist(@RequestParam String username) throws Exception {
        return super.dispatcher.dispatch(new ParamWrapper<String>(username), FunctionEnum.QUERY_USER_SERVER);

    }


    @GetMapping("/getImageCode")
    public Response getImageCode( @RequestParam Long robotId) throws Exception {
        if (null == robotId) {
            return Response.FAIL("未传入robotId");
        }

        return super.dispatcher.dispatch(new ParamWrapper<Long>(robotId),FunctionEnum.Image_CODE_SERVER);

    }



    @RabbitListener(queues = RabbitMqConstants.REMIT_QUEUE_95_CARD)
    @RabbitHandler
	public void payAmountMq(TaskAtomDto taskAtomDto, Channel channel, Message message) {
		try {
			super.handleTenant(RobotConsts.PLATFORM_ID.JIU_WU_CARD,RobotConsts.FUNCTION_CODE.ACTIVITY);
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
}
