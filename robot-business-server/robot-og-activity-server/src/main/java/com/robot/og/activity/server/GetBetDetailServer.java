package com.robot.og.activity.server;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbin.common.client.BetQueryDto;
import com.robot.code.entity.VsGame;
import com.robot.code.response.Response;
import com.robot.code.service.impl.VsGameServiceImpl;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.bo.BetDetailBO;
import com.robot.og.base.bo.TenantBetDetailBO;
import com.robot.og.base.bo.TenanteBetBO;
import com.robot.og.base.common.Constant;
import com.robot.og.base.function.GetBetDetailFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 查询下注详情
 * </p>
 *
 * @author tanke
 * @date 2020/7/13
 */

@Service
public class GetBetDetailServer implements IAssemFunction<BetQueryDto> {

	@Autowired
	private VsGameServiceImpl gameService;
	@Autowired
	private GetBetDetailFunction getBetDetailFunction;

	@Override
	public Response doFunction(ParamWrapper<BetQueryDto> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		Response<TenantBetDetailBO> getBetDetailBOResponse = getBetDetailFunction.doFunction(paramWrapper,robotWrapper);
		if (!getBetDetailBOResponse.isSuccess()) {
			return getBetDetailBOResponse;
		}

		return getBetDetailBOResponse;
	}
	private List<VsGame> getGameids(){
		List<VsGame> vsGames = gameService.list(new QueryWrapper<VsGame>().select("game_id").eq("status", Constant.ID));

		return 	vsGames;
	}
}
