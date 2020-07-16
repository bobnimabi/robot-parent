package com.robot.jiuwu.activity.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.client.BetQueryDto;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.dto.robot.BreakThroughDTO;
import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.bbin.common.pojo.TaskAtomDto;
import com.bbin.common.response.ResponseResult;
import com.bbin.common.util.DateUtils;
import com.rabbitmq.client.Channel;
import com.robot.center.constant.RobotConsts;
import com.robot.center.controller.ControllerBase;
import com.robot.code.dto.LoginDTO;
import com.robot.code.response.Response;
import com.robot.core.common.TContext;
import com.robot.core.function.base.ParamWrapper;
import com.robot.jiuwu.base.basic.FunctionEnum;
import com.robot.jiuwu.base.basic.PathEnum;
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
public class JiuWuController extends ControllerBase {

	/**
	 * 获取图片验证码
	 * @param robotId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getImageCode")
	public Response getImageCode( @RequestParam Long robotId) throws Exception {
		if (null == robotId) {
			return Response.FAIL("未传入robotId");
		}
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setId(robotId);
		return super.dispatcher.disPatcherLogin(new ParamWrapper<LoginDTO>(loginDTO),FunctionEnum.Image_CODE_SERVER,true);

	}

	/**
	 * 机器人登录
	 * 注意：登录的第一个接口isNewCookie：true
	 */
	@PostMapping("/robotLogin")
	public Response robotLogin(@RequestBody LoginDTO loginDTO) throws Exception {
		if (null == loginDTO || null == loginDTO.getId()
				||null==loginDTO.getImageCode()
				||null==loginDTO.getOpt()
		) {
			return Response.FAIL("未传入参数");
		}
		Response response = super.dispatcher.disPatcherLogin(new ParamWrapper(loginDTO), FunctionEnum.LOGIN_SERVER, false);
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



	// 获取vip和总打码量
	@PostMapping("/getVipAndTotalAmount")
	public Response getVipAndTotalAmount(@RequestBody VipTotalAmountDTO vipTotalAmountDTO) throws Exception{
		if (StringUtils.isEmpty(vipTotalAmountDTO.getUserName())) {
			return Response.FAIL("未传入UserName");
		}
		if (StringUtils.isEmpty(vipTotalAmountDTO.getBeginDate())) {
			return Response.FAIL("未传入BeginDate");
		}
		if (StringUtils.isEmpty(vipTotalAmountDTO.getEndDate())) {
			return Response.FAIL("未传入EndDate");
		}

		return super.dispatcher.dispatch(new ParamWrapper<VipTotalAmountDTO>(vipTotalAmountDTO),FunctionEnum.QUERY_VIP_AMOUNT_SERVER);
	}

	// 查询总打码量
	@PostMapping("/QueryTotalRecharge")
	public Response QueryTotalRecharge(@RequestBody VipTotalAmountDTO vipTotalAmountDTO) throws Exception {
		if (null==(vipTotalAmountDTO.getUserName())) {
			return Response.FAIL("未传入gameid");
		}
		if (StringUtils.isEmpty(vipTotalAmountDTO.getEndDate())) {
			return Response.FAIL("未传入endTime");
		}
		if (StringUtils.isEmpty(vipTotalAmountDTO.getBeginDate())) {
			return Response.FAIL("未传入startTime");
		}
		return super.dispatcher.dispatch(new ParamWrapper<VipTotalAmountDTO>(vipTotalAmountDTO), FunctionEnum.TOTAL_RECHARGE_SERVER);
	}



//	@ApiOperation("机器人：获取实际投注详细")
	@PostMapping("/getBetDetail")
	public Response getBetDetail(
			@RequestBody BetQueryDto betQueryDto
	) throws Exception {
		BreakThroughDTO br = new BreakThroughDTO();
		br.setUserName(betQueryDto.getUserName());
		br.setBeginDate(DateUtils.format(betQueryDto.getStartDate()));
		br.setEndDate(DateUtils.format(betQueryDto.getEndDate()));

		return getTotalAmount(br);
	}



//	@ApiOperation("机器人：获取投注、亏损、充值信息")
	@PostMapping("/getTotalAmount")
	public Response getTotalAmount(@RequestBody BreakThroughDTO dto) throws Exception {
		if (StringUtils.isEmpty(dto.getUserName())) {
			return Response.FAIL("userName为空");
		}
		if (StringUtils.isEmpty(dto.getBeginDate())) {
			return Response.FAIL("起始时间为空");
		}
		if (StringUtils.isEmpty(dto.getEndDate())) {
			return Response.FAIL("结束时间为空");
		}

		return super.dispatcher.dispatch(new ParamWrapper<BreakThroughDTO>(dto), FunctionEnum.BET_AMOUNT_AND_RECHARGE_SERVER);
	}


	/**
	 * 打款
	 * @param taskAtomDto
	 * @param channel
	 * @param message
	 */
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

	/**
	 * 测试用 生产环境不用
	 * @param taskAtomDto
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
