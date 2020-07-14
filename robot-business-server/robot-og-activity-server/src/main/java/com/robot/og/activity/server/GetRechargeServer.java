package com.robot.og.activity.server;

import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.function.GetLostDetailFunction;
import com.robot.og.base.function.GetRechargeFunction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/13
 */
public class GetRechargeServer  implements IAssemFunction<VipTotalAmountDTO> {


	@Autowired
	private GetRechargeFunction getRechargeFunction;

	@Override
	public Response doFunction(ParamWrapper<VipTotalAmountDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		return null;
	}
}
