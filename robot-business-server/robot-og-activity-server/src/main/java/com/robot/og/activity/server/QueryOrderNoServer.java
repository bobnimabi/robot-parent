package com.robot.og.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.QueryOrderNoAO;
import com.robot.og.base.ao.QueryUserAO;
import com.robot.og.base.function.QueryLevelFunction;
import com.robot.og.base.function.QueryOrderNoFunction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/14
 */
public class QueryOrderNoServer implements IAssemFunction<OrderNoQueryDTO> {

	@Autowired
	private QueryOrderNoFunction queryOrderNoFunction;

	@Override
	public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		Response<String> response = queryOrderNoFunction.doFunction(creatParams(paramWrapper),robotWrapper);
		// todo  校验日期
		return response;
	}

	/**
	 * 查询用户参数组装
	 */
	private ParamWrapper<QueryOrderNoAO> creatParams(ParamWrapper<OrderNoQueryDTO> paramWrapper) {
		OrderNoQueryDTO noQueryDTO = paramWrapper.getObj();
		QueryOrderNoAO ao = new QueryOrderNoAO();
		ao.setType("queryMemberReportDetail");
		ao.setAccountId(noQueryDTO.getOrderNo());
		ao.setType("queryMemberReportDetail");
		ao.setType("queryMemberReportDetail");
		ao.setType("queryMemberReportDetail");


		return new ParamWrapper<QueryOrderNoAO>(ao);
	}

}
