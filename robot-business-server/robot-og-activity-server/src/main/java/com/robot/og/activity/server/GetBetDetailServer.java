package com.robot.og.activity.server;


import com.bbin.common.dto.robot.BreakThroughDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.bo.BetDetailBO;
import com.robot.og.base.function.GetBetDetailFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;


/**
 * <p>
 * 查询下注详情
 * </p>
 *
 * @author tanke
 * @date 2020/7/13
 */

@Service
public class GetBetDetailServer implements IAssemFunction<BreakThroughDTO> {

	@Autowired
	private GetBetDetailFunction getBetDetailFunction;

	@Override
	public Response doFunction(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		Response<BetDetailBO> getBetDetailBOResponse = getBetDetailFunction.doFunction(paramWrapper,robotWrapper);
		if (!getBetDetailBOResponse.isSuccess()) {
			return getBetDetailBOResponse;
		}
		return getBetDetailBOResponse;
	}

}
