package com.robot.og.activity.server;

import com.bbin.common.client.BetQueryDto;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.dto.robot.BreakThroughDTO;
import com.bbin.common.util.DateUtils;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.bo.BetDetailBO;
import com.robot.og.base.bo.AmountBO;
import com.robot.og.base.function.GetBetDetailFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * <p>
 * 查询充值和下注详情
 * </p>
 *
 * @author tank
 * @date 2020/7/13
 */

@Service
public class GetAmountServer implements IAssemFunction<BreakThroughDTO> {

	@Autowired
	private GetRechargeServer getRechargeServer;

	@Autowired
	private GetBetDetailFunction getBetDetailFunction;


	@Override
	public Response doFunction(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

		//查询充值信息

		Response <String> reChargeRsult = getRechargeServer.doFunction(paramWrapper,robotWrapper);
		if (!reChargeRsult.isSuccess()){
			return Response.FAIL("查询充值失败");
		}
		String reCharge = reChargeRsult.getObj();

		//下注详情查询
		Response<BetDetailBO> betDetailResponse = getBetDetailFunction.doFunction(createBetParams(paramWrapper), robotWrapper);
		if (!betDetailResponse.isSuccess()) {
			return Response.FAIL("未查询到下注信息");
		}
		BetDetailBO betDetailBO = betDetailResponse.getObj();

		AmountBO amountBO = new AmountBO();
		amountBO.setInome(reCharge);
		amountBO.setTenantBets(betDetailBO.getTenantBets());
		amountBO.setTotalBet(betDetailBO.getTotalBet());
		amountBO.setTotalLoss(betDetailBO.getTotalLoss());

		return Response.SUCCESS(amountBO);

	}

	/**
	 * 组装投注查询参数
	 * @param paramWrapper
	 * @return
	 */
	private ParamWrapper<BetQueryDto> createBetParams(ParamWrapper<BreakThroughDTO> paramWrapper) {
		BreakThroughDTO dto = paramWrapper.getObj();
		BetQueryDto betQueryDto = new BetQueryDto();
		betQueryDto.setUserName(dto.getUserName());
		betQueryDto.setStartDate(DateUtils.format(dto.getBeginDate()));
		betQueryDto.setEndDate(DateUtils.format(dto.getEndDate()));
		betQueryDto.setGameList(dto.getGameCodeList());

		return new ParamWrapper<BetQueryDto>(betQueryDto);
	}

}