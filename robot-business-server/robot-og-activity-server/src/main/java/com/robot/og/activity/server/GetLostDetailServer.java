package com.robot.og.activity.server;

import com.bbin.common.client.BetQueryDto;
import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.GetBetDetailAO;
import com.robot.og.base.bo.GetBetDetailBO;
import com.robot.og.base.function.GetBetDetailFunction;
import com.robot.og.base.function.GetLostDetailFunction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  查询损益
 * </p>
 *
 * @author tank
 * @date 2020/7/14
 */
public class GetLostDetailServer  implements IAssemFunction<BetQueryDto> {

	@Autowired
	private GetBetDetailFunction getBetDetailFunction;

	@Override
	public Response doFunction(ParamWrapper<BetQueryDto> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		Response<GetBetDetailBO> getBetDetailBOResponse = getBetDetailFunction.doFunction(createBetDetailParams(paramWrapper), robotWrapper);
		if (!getBetDetailBOResponse.isSuccess()) {
			return getBetDetailBOResponse;
		}
		return getBetDetailBOResponse;
	}

	/**
	 * 组装查询下注详情参数    /
	 *
	 * @param paramWrapper
	 * @return
	 */


	private ParamWrapper<GetBetDetailAO> createBetDetailParams(ParamWrapper<BetQueryDto> paramWrapper) {
		BetQueryDto betQueryDto = paramWrapper.getObj();
		GetBetDetailAO ao = new GetBetDetailAO();
		ao.setAccount(betQueryDto.getUserName());
		ao.setStartDate(betQueryDto.getStartDate());
		ao.setEndDate(betQueryDto.getEndDate());
		ao.setGameCode(betQueryDto.getGameList().toString());  //todo

		return new ParamWrapper<GetBetDetailAO>(ao);
	}
}