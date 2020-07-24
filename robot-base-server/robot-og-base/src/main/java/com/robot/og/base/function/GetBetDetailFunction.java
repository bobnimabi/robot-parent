package com.robot.og.base.function;


import com.bbin.common.dto.robot.BreakThroughDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.basic.PathEnum;
import com.robot.og.base.bo.BetDetailBO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



/**
 * Created by tanke on 11/14/2019 8:06 PM
 * 下注详情
 */
@Slf4j
@Service
public class GetBetDetailFunction extends AbstractFunction<BreakThroughDTO, String, BetDetailBO> {

	@Override
	protected IPathEnum getPathEnum() {
		return PathEnum.GET_DETAIL;
	}


	@Override
	protected IEntity getEntity(BreakThroughDTO dto, RobotWrapper robotWrapper) {


		//gamelist可以为空
		return UrlEntity.custom(6)
				.add("account", dto.getUserName())
				.add("startDate", dto.getBeginDate())
				.add("lastDate", dto.getEndDate())
				.add("plat", dto.getGameCodeList().get(0))

				;

	}

	@Override
	protected IResultHandler<String, BetDetailBO> getResultHandler() {
		return ResultHandler.INSTANCE;
	}

	/**
	 * 响应结果转换：
	 * 存在返回：
	 */
	private static final class ResultHandler implements IResultHandler<String, BetDetailBO> {
		private static final ResultHandler INSTANCE = new ResultHandler();

		private ResultHandler() {
		}

		@Override
		public Response parse2Obj(StanderHttpResponse<String, BetDetailBO> shr) {
			String result = shr.getOriginalEntity();
			log.info("查询下注详情功能响应：{}");
			if (StringUtils.isEmpty(result)) {
				return Response.FAIL("未查询到下注记录");
			}
			Document doc = Jsoup.parse(result);

			BetDetailBO betDetailBO = new BetDetailBO();
			Elements select = doc.select("tbody>tr>td");

			//用户名
			String userName = select.get(0).text();

			//总投注
			String totalBet = select.get(3).text();

			//当前游戏投注
			String currentBet = select.get(8).text();

			//当前游戏损益
			String currentLoss = select.get(9).text();
			betDetailBO.setUserName(userName);
			betDetailBO.setTotalBet(totalBet);
			betDetailBO.setTenantBets(currentBet);
			betDetailBO.setTotalLoss(currentLoss);

			return Response.SUCCESS(betDetailBO);

		}
	}

}


