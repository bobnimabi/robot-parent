package com.robot.og.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.project.DateUtils;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.QueryOrderNoAO;
import com.robot.og.base.ao.QueryUserAO;
import com.robot.og.base.bo.QueryUserBO;
import com.robot.og.base.function.QueryOrderNoFunction;
import com.robot.og.base.function.QueryUserFunction;
import com.robot.og.base.server.QueryUserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *查询注单
 * </p>
 *
 * @author tank
 * @date 2020/7/14
 */
@Service
public class OrderQueryServer implements IAssemFunction<OrderNoQueryDTO> {

	@Autowired
	private QueryOrderNoFunction queryOrderNoFunction;

	@Autowired
	private QueryUserFunction queryUserFunction;

	@Override
	public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

		//查询用户是否存在获取 memberid   ==accountid
		Response<QueryUserBO> responseResult = queryUserFunction.doFunction(creatQueryUserParams(paramWrapper), robotWrapper);
		if(!responseResult.isSuccess()){
			return Response.FAIL("查询用户id失败");
		}
		QueryUserBO queryUserBO = responseResult.getObj();

		Response<String> response = queryOrderNoFunction.doFunction(creatParams(paramWrapper,queryUserBO), robotWrapper);


		return response;
	}

	/**
	 * 查询用户参数组装
	 */
	private ParamWrapper<QueryOrderNoAO> creatParams(ParamWrapper<OrderNoQueryDTO> paramWrapper,QueryUserBO queryUserBO) {
		OrderNoQueryDTO noQueryDTO = paramWrapper.getObj();
		QueryOrderNoAO ao = new QueryOrderNoAO();
		ao.setType("queryMemberReportDetail");
		ao.setAccountId(queryUserBO.getMemberId() );
		ao.setBettingCode(noQueryDTO.getOrderNo());
		ao.setPlatform(noQueryDTO.getGameCode());
		ao.setStartDate(DateUtils.format(noQueryDTO.getStartDate()));
		ao.setLastDate(DateUtils.format(noQueryDTO.getEndDate()));
		ao.setPageNo("1");
		ao.setPageSize("1000");




		return new ParamWrapper<QueryOrderNoAO>(ao);
	}

	/**
	 * 查询用户参数组装
	 * @return
	 */
	private ParamWrapper<QueryUserAO> creatQueryUserParams(ParamWrapper<OrderNoQueryDTO> paramWrapper) {
		OrderNoQueryDTO noQueryDTO = paramWrapper.getObj();
		QueryUserAO ao = new QueryUserAO();
		ao.setType("queryManDeposit");
		ao.setAccount(noQueryDTO.getUserName());


		return new ParamWrapper<>(ao);
	}


}
