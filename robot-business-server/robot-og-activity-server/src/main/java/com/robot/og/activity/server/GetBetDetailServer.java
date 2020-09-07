package com.robot.og.activity.server;

import com.bbin.common.client.BetQueryDto;
import com.robot.code.entity.VsGame;
import com.robot.code.response.Response;
import com.robot.code.service.impl.VsGameServiceImpl;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.bo.TenantBetDetailBO;
import com.robot.og.base.bo.TenanteBetBO;
import com.robot.og.base.function.GetBetDetailFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;



/**
 * <p>
 * 查询下注详情
 * </p>
 * @author tanke
 * @date 2020/7/13
 */

@Service
public class GetBetDetailServer implements IAssemFunction<BetQueryDto> {

	@Autowired
	@Lazy
	private VsGameServiceImpl gameService;
	@Autowired
	private GetBetDetailFunction getBetDetailFunction;

	@Override
	public Response doFunction(ParamWrapper<BetQueryDto> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		Response<TenantBetDetailBO> getBetDetailBOResponse = getBetDetailFunction.doFunction(paramWrapper,robotWrapper);
		if (!getBetDetailBOResponse.isSuccess()) {
			return Response.FAIL(getBetDetailBOResponse.getMessage());
		}
		TenantBetDetailBO betDetailBO = getBetDetailBOResponse.getObj();

		List<TenanteBetBO> tenantBets = betDetailBO.getTenantBets();

		HashMap<String, String> gameIdMap = getGameId();
		for (TenanteBetBO tenantBet : tenantBets) {
			tenantBet.setGameId(gameIdMap.get(tenantBet.getGameId()));
		}

		return getBetDetailBOResponse;
	}


	private HashMap<String ,String> getGameId(){
		List<VsGame> list = gameService.list();
		HashMap<String, String> idMap = new HashMap<>();
		for (VsGame vsGame : list) {
			idMap.put(vsGame.getGameCode(),vsGame.getGameId().toString());
		}
		return idMap;
	}

}
