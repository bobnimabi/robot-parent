package com.robot.og.activity.server;


import com.bbin.common.client.BetQueryDto;
import com.bbin.common.dto.robot.BreakThroughDTO;
import com.bbin.common.util.DateUtils;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.GetBetDetailAO;
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
public class GetBetDetailServer implements IAssemFunction<BreakThroughDTO> {   //参数需要修改

	@Autowired
	private GetBetDetailFunction getBetDetailFunction;

	@Override
	public Response doFunction(ParamWrapper<BreakThroughDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
		Response<Map<String, Map<String, String>>> getBetDetailBOResponse = getBetDetailFunction.doFunction(paramWrapper,robotWrapper);
		if (!getBetDetailBOResponse.isSuccess()) {
			return getBetDetailBOResponse;
		}
		return getBetDetailBOResponse;
	}
	/**
	 * 组装查询下注详情参数
	 * @param
	 * @return
	 */

	/*private ParamWrapper<GetBetDetailAO> createBetDetailParams(ParamWrapper<BreakThroughDTO> paramWrapper) {
		BreakThroughDTO dto = paramWrapper.getObj();

		BetQueryDto betQueryDto = new BetQueryDto();
			betQueryDto.setUserName(dto.getUserName());
			betQueryDto.setStartDate(DateUtils.format(dto.getBeginDate()));
			betQueryDto.setEndDate(DateUtils.format(dto.getEndDate()));
			betQueryDto.setGameList( dto.getGameCodeList());

		GetBetDetailAO ao = new GetBetDetailAO();
		ao.setAccount(betQueryDto.getUserName());
		ao.setStartDate(betQueryDto.getStartDate());
		ao.setEndDate(betQueryDto.getEndDate());
		ao.setPlat("BBJL");
		ao.setPlat("BBJL");
		ao.setPlat("BBCP");
		ao.setPlat("AGBY");
		ao.setPlat("AGJL");
		ao.setPlat("NMGJL");
		ao.setPlat("PTJL");
		ao.setPlat("MWGJL");
		ao.setPlat("PPJL");
		ao.setPlat("CQ9JL");
		ao.setPlat("BGBY");
		ao.setPlat("BGJL");
		ao.setPlat("PNGJL");
		ao.setPlat("JDBJL");
		ao.setPlat("FGBY");
		ao.setPlat("FGJL");
		ao.setPlat("DTJL");
		ao.setPlat("WMJL");
		ao.setPlat("JDBBY");
		ao.setPlat("BSPBY");
		ao.setPlat("PGJL");
		ao.setPlat("SGJL");
		ao.setPlat("MGPJL");
		ao.setPlat("SCGBY");
		ao.setPlat("SCGJL");
		ao.setPlat("YOPLAYJL");
		ao.setPlat("THBY");
		ao.setPlat("THJL");

		String [] strArray = {"ALL","BBJL","BBCP","AGBY","AGJL","NMGJL","PTJL","MWGJL","PPJL","CQ9JL","BGBY","BGJL","PNGJL","JDBJL","FGBY","FGJL","DTJL","WMJL","JDBBY","BSPBY","PGJL","SGJL","MGPJL","SCGBY","SCGJL","YOPLAYJL","THBY","THJL"};


	*//*	List<String> gameList = betQueryDto.getGameList();
		for (String s : gameList) {
			ao.setPlat(s);
		}*//*


		return new ParamWrapper<GetBetDetailAO>(ao);*/


}
