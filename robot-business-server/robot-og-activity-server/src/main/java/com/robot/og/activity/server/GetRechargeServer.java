package com.robot.og.activity.server;


import com.bbin.common.dto.robot.BreakThroughDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.function.GetRechargeFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/13
 */

@Service
public class GetRechargeServer  implements IAssemFunction<BreakThroughDTO> {


	@Autowired
	private GetRechargeFunction getRechargeFunction;

	@Override
	public Response doFunction(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

		Response <String> reChargeRsult=getRechargeFunction.doFunction(paramWrapper,robotWrapper);
		if(!reChargeRsult.isSuccess()){
			return reChargeRsult;
		}

		return reChargeRsult ;
	}


}
