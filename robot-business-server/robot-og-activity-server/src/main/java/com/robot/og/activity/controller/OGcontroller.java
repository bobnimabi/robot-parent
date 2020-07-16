package com.robot.og.activity.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.client.BetQueryDto;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.dto.robot.BreakThroughDTO;
import com.bbin.common.pojo.CashDetailDTO;
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
import com.robot.og.base.basic.FunctionEnum;
import com.robot.og.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 操作接口
 * </p>
 *
 * @author tank
 * @date 2020/7/12
 */

@Slf4j
@RestController
public class OGcontroller extends ControllerBase {

	/**
	 * 获取图片验证码
	 *
	 * @param robotId
	 * @return            OK
	 * @throws Exception
	 */
	@GetMapping("/getImageCode")
	public Response getImageCode(@RequestParam Long robotId) throws Exception {
		if (null == robotId) {
			return Response.FAIL("未传入robotId");
		}
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setId(robotId);
		return super.dispatcher.disPatcherLogin(new ParamWrapper<LoginDTO>(loginDTO), FunctionEnum.IMAGE_CODE_SERVER, true);

	}

	/**
	 * 机器人登录
	 * 注意：登录的第一个接口isNewCookie：true
	 */
	@PostMapping("/robotLogin")
	public Response robotLogin(@RequestBody LoginDTO loginDTO) throws Exception {
		if (null == loginDTO
				|| null == loginDTO.getId()
				||null==loginDTO.getImageCode()
				||null==loginDTO.getOpt()) {
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
		if(null==username){
			return Response.FAIL("未传入用户名");
		}
		return super.dispatcher.dispatch(new ParamWrapper<String>(username), FunctionEnum.QUERY_USER_SERVER);
	}




	/* ------------分割线------------------------*/

	//	@ApiOperation("机器人：获取实际投注详细")
	@PostMapping("/getBetDetail")
	public Response getBetDetail(@RequestBody BetQueryDto dto) throws Exception {
		if(null==dto.getUserName()
		||null==dto.getStartDate()
		|| null==dto.getEndDate()){
			return Response.FAIL("参数不完整");
		}

		return super.dispatcher.dispatch(new ParamWrapper<BetQueryDto>(dto),FunctionEnum.GETDETAIL_SERVER);
	}



	//	@ApiOperation("机器人：获取投注和充值信息")
	@PostMapping("/getTotalAmount")
	public Response getTotalAmount(@RequestBody BreakThroughDTO dto) throws Exception {
		if (StringUtils.isEmpty(dto.getUserName())
		||StringUtils.isEmpty(dto.getBeginDate())
		||StringUtils.isEmpty(dto.getEndDate())) {
			return Response.FAIL("未传入参数");
		}

		return super.dispatcher.dispatch(new ParamWrapper<BreakThroughDTO>(dto),FunctionEnum.GETTOTAL_AMOUNT_SERVER);
	}


	@PostMapping("/getRecharge")
	//@ApiOperation("查询：获取充值信息")     OK
	public Response getRecharge(@RequestBody BreakThroughDTO incomeInfoDTO) throws Exception{
		if (null == incomeInfoDTO
				|| StringUtils.isEmpty(incomeInfoDTO.getUserName())
				|| StringUtils.isEmpty(incomeInfoDTO.getBeginDate())
				|| StringUtils.isEmpty(incomeInfoDTO.getEndDate())) {
			return Response.FAIL("参数不全");
		}

		return super.dispatcher.dispatch(new ParamWrapper<BreakThroughDTO>(incomeInfoDTO),FunctionEnum.GETRECHARGE_SERVER);
	}

	//@ApiOperation("机器人：获取投注损益详细")
	@PostMapping("/getLostDetail")
	public Response getLostDetail(@RequestBody BetQueryDto dto) throws Exception {
		if (null == dto
				|| null == dto.getUserName()
				|| null == dto.getStartDate()
				|| null == dto.getEndDate()) {
			return Response.FAIL("参数不全");

		}
		return super.dispatcher.dispatch(new ParamWrapper<BetQueryDto>(dto), FunctionEnum.GETLOST_DETAIL_SERVER);
	}

/*
	//查询用户是否存在
	@GetMapping("/isExist")
	public Response isExist(@RequestParam String username) throws Exception {

		return null;
	}*/

	//查询余额
	@GetMapping("/queryBalance")
	public Response queryBalance(@RequestParam String userName) throws Exception {
		if(StringUtils.isEmpty(userName)){
			return Response.FAIL("用户名有误");
		}

		return super.dispatcher.dispatch(new ParamWrapper<String>(userName),FunctionEnum.QUERY_BALANCE_SERVER);
	}

	//查询层级信息
	@GetMapping("queryLevel")
	public Response queryLevel() throws Exception {

		return super.dispatcher.dispatch(new ParamWrapper<>(),FunctionEnum.QUERY_LEVEL_SERVER);
	}



	/**
	 * 查询
	 * @param orderNoQueryDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("queryOrderNo")
	public Response queryOrderNo(@RequestBody OrderNoQueryDTO orderNoQueryDTO) throws Exception{
		if (null == orderNoQueryDTO
				|| StringUtils.isEmpty(orderNoQueryDTO.getUserName())
				|| StringUtils.isEmpty(orderNoQueryDTO.getGameCode())
				|| StringUtils.isEmpty(orderNoQueryDTO.getOrderNo())
				|| null == orderNoQueryDTO.getStartDate()
				|| null == orderNoQueryDTO.getEndDate()
		){
			return Response.FAIL("参数不全");
		}

		return super.dispatcher.dispatch(new ParamWrapper<OrderNoQueryDTO>(orderNoQueryDTO),FunctionEnum.QUERY_ODERNO_SERVER);
	}




	/**
	 * 大转盘用
	 * @param userName
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	@GetMapping("queryAccount")
	public Response queryAccount(@RequestParam String userName, @RequestParam(required = false ) String beginTime, @RequestParam(required = false ) String endTime) throws Exception{
		if (StringUtils.isEmpty(userName)) {
			return Response.FAIL("未传入会员账号");
		}
		return super.dispatcher.dispatch(new ParamWrapper<BreakThroughDTO>(),FunctionEnum.QUERY_ACCOUNT_SERVER);  //需修改
	}


	//@ApiOperation("查询会员信息")
	@GetMapping("/queryUserInfo")
	public Response queryUserInfo(@RequestParam String userName) throws Exception {
		if (StringUtils.isEmpty(userName)) {
			return Response.FAIL("未传入会员账号");
		}
		return super.dispatcher.dispatch(new ParamWrapper<String>(userName),FunctionEnum.QUERY_USER_INFO);
	}


	//@ApiOperation("子现金：查询会员详细")
	@PostMapping("/queryUserRecord")
	public Response queryUserRecord(@RequestBody CashDetailDTO cashDetailDTO) throws Exception {
		if (null == cashDetailDTO ||  StringUtils.isBlank(cashDetailDTO.getUserName())) {
			return Response.FAIL("参数不全");
		}
		if (null == cashDetailDTO.getEndTime()) {
			cashDetailDTO.setEndTime(DateUtils.format(LocalDateTime.now().minusDays(7)));
		}
		if (null == cashDetailDTO.getStartTime()) {
			cashDetailDTO.setStartTime(DateUtils.format(LocalDateTime.now()));
		}
		return super.dispatcher.dispatch(new ParamWrapper<CashDetailDTO>(cashDetailDTO),FunctionEnum.QUERY_USERRECORD_SERVER);
	}



/*----------------------------分割线---------------------------------------*/



	/**
	 * 打款
	 *
	 * @param taskAtomDto
	 * @param channel
	 * @param message
	 */
	@RabbitListener(queues = RabbitMqConstants.REMIT_QUEUE_OG)
	@RabbitHandler
	public void payAmountMq(TaskAtomDto taskAtomDto, Channel channel, Message message) {
		try {
			super.handleTenant(RobotConsts.PLATFORM_ID.OG, RobotConsts.FUNCTION_CODE.ACTIVITY);
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
				log.error("金额不能小于等于0,paidAmount:" + taskAtomDto.getPaidAmount());
				return;
			}
			taskAtomDto.setUsername(taskAtomDto.getUsername().trim());
			super.dispatcher.asyncDispatch(new ParamWrapper(taskAtomDto), taskAtomDto.getOutPayNo(), PathEnum.PAY, FunctionEnum.PAY_SERVER);
		} catch (Exception e) {
			log.info("机器人：MQ打款异常", e);
		} finally {
			if (null != channel) {
				try {
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				} catch (IOException e) {
					log.error("teantId:{},channelId:{},mq的Ack异常", TContext.getTenantId(), TContext.getChannelId(), e);
				}
			}
			TContext.clean();
		}

	}

	/**
	 * 测试用 生产环境不用
	 *
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
			log.error("金额不能小于等于0,paidAmount:" + taskAtomDto.getPaidAmount());
			return;
		}
		taskAtomDto.setUsername(taskAtomDto.getUsername().trim());
		super.dispatcher.asyncDispatch(new ParamWrapper(taskAtomDto), taskAtomDto.getOutPayNo(), PathEnum.PAY, FunctionEnum.PAY_SERVER);
	}

}
