package com.robot.og.activity.server;


import com.bbin.common.dto.robot.BreakThroughDTO;
import com.bbin.common.dto.robot.OGBreakThroughDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.function.GetBetDetailFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;


/**
 * <p>
 * 查询下注详情
 * </p>
 *
 * @author tank
 * @date 2020/7/13
 */

@Service
public class GetBetDetailServer implements IAssemFunction<OGBreakThroughDTO> {

	@Autowired
	private GetBetDetailFunction getBetDetailFunction;

	@Override
	public Response doFunction(ParamWrapper<OGBreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		Response<Map<String, Map<String, String>>> getBetDetailBOResponse = getBetDetailFunction.doFunction(paramWrapper,robotWrapper);
		if (!getBetDetailBOResponse.isSuccess()) {
			return getBetDetailBOResponse;
		}
		return getBetDetailBOResponse;
	}

}
