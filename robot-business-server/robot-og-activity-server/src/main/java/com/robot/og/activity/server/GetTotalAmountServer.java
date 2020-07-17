package com.robot.og.activity.server;

import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.function.GetRechargeFunction;
import com.robot.og.base.function.GetTotalAmountFunction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/13
 */
public class GetTotalAmountServer implements IAssemFunction<VipTotalAmountDTO> {

	@Autowired
	private GetTotalAmountFunction getTotalAmountFunction;

	@Override
	public Response doFunction(ParamWrapper<VipTotalAmountDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		return null;
	}
}
