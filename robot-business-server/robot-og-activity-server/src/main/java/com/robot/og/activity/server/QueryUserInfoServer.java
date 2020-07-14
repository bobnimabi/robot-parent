package com.robot.og.activity.server;

import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.function.QueryOrderNoFunction;
import com.robot.og.base.function.QueryUserInfoFunction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/14
 */
public class QueryUserInfoServer implements IAssemFunction<VipTotalAmountDTO> {

	@Autowired
	private QueryUserInfoFunction getRechargeFunction;
	@Override
	public Response doFunction(ParamWrapper<VipTotalAmountDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		return null;
	}
}
