package com.robot.og.activity.server;

import com.bbin.common.dto.robot.BreakThroughDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.QueryRechargeAO;
import com.robot.og.base.function.QueryUserRecordFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/14
 */
@Service
public class QueryUserRecordServer implements IAssemFunction<BreakThroughDTO> {

	@Autowired
	private QueryUserRecordFunction queryUserRecordFunction;
	@Override
	public Response doFunction(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		Response <String> reChargeRsult=queryUserRecordFunction.doFunction(createRechargeParams(paramWrapper),robotWrapper);
		if(!reChargeRsult.isSuccess()){
			return reChargeRsult;
		}

		return reChargeRsult ;
	}


	private ParamWrapper<QueryRechargeAO> createRechargeParams(ParamWrapper<BreakThroughDTO> paramWrapper) {
		BreakThroughDTO breakThroughDTO = paramWrapper.getObj();
		QueryRechargeAO rechargeAO = new QueryRechargeAO();
		rechargeAO.setType("queryRecord");
		rechargeAO.setTradeTypes("");
		rechargeAO.setIsPostback("1");
		rechargeAO.setOrderField("createDateTime");
		rechargeAO.setSortBy("DESC");
		rechargeAO.setSelDate("0");
		rechargeAO.setStartDate(breakThroughDTO.getBeginDate());
		rechargeAO.setEndDate(breakThroughDTO.getEndDate());
		rechargeAO.setActType("queryRecord");
		rechargeAO.setType("0");
		rechargeAO.setMemberNo(breakThroughDTO.getUserName());
		rechargeAO.setPageSize("20");


		return new ParamWrapper<QueryRechargeAO>(rechargeAO);
	}

}
