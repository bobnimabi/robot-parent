package com.robot.og.base.function;


import com.bbin.common.client.BetQueryDto;
import com.bbin.common.client.TenantBetVo;
import com.bbin.common.util.DateUtils;
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
import com.robot.og.base.bo.TatolLossBO;
import com.robot.og.base.bo.TenanteBetBO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by tanke on 11/14/2019 8:06 PM
 * 下注详情   BreakThroughDTO
 */
@Slf4j
@Service
public class GetBetDetailFunction extends AbstractFunction<BetQueryDto, String, BetDetailBO> {

	@Override
	protected IPathEnum getPathEnum() {
		return PathEnum.GET_DETAIL;
	}


	@Override
	protected IEntity getEntity(BetQueryDto dto, RobotWrapper robotWrapper) {


		//gamelist可以为空
		UrlEntity entity = UrlEntity.custom(4)
				.add("account", dto.getUserName())
				.add("startDate", DateUtils.format(dto.getStartDate()))
				.add("lastDate", DateUtils.format(dto.getEndDate()))
				;
		List<String> gameList = dto.getGameList();
		for (String s : gameList) {
			entity.add("plat",s);
		}

		return entity;
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
			Elements tds = doc.select("tbody>tr>td");
			Elements tbody = doc.select("tbody");
			Element element = tbody.get(0);
			TatolLossBO tatolLossBO = new TatolLossBO();
			tatolLossBO.setBalance(new BigDecimal(0));
			tatolLossBO.setTotalLoss(new BigDecimal(tds.get(4).text()));

			TenanteBetBO tenantBetVo = new TenanteBetBO();
			for (int i = 1; i < tbody.size() - 1; i++) {
				Element element1 = tbody.get(i);
				Elements td = element1.select("tr>td");
				tenantBetVo.setTenantId(1L);
				//数字多少没有关系
				tenantBetVo.setGameId(1L);
				tenantBetVo.setLossAmount(new BigDecimal(td.get(4).text()));
				List<TenanteBetBO> list = (List<TenanteBetBO>) Arrays.asList(tenantBetVo);
				if (tenantBetVo.getLossAmount().intValue() != 0) {
					tatolLossBO.setTenantBets(list);
				}

			}

			return Response.SUCCESS(tatolLossBO);
		}
	}

		/* 测试解析
		public static void main(String[] args) throws IOException {

		Document doc = Jsoup.parse(new File("C:\\Users\\8888\\IdeaProjects\\robot-parent\\robot-business-server\\robot-og-activity-server\\src\\main\\resources\\test.html"), "utf-8");
		Elements tds = doc.select("tbody>tr>td");
		Elements tbody = doc.select("tbody");
		Element element = tbody.get(0);
		int size = tbody.size();

		System.out.println("size = " + size);

		BetDetailBO betDetailBO = new BetDetailBO();

		betDetailBO.setBalance(new BigDecimal(0));
		//	betDetailBO.setTotalBet(new BigDecimal(select.get(3).text()));

		betDetailBO.setTotalLoss(new BigDecimal(tds.get(4).text()));
		System.out.println(tds.get(4).text());
//----------------------------------------------------//

		TenanteBetBO tenantBetVo = new TenanteBetBO();


		for (int i = 1; i < tbody.size() - 1; i++) {
			Element element1 = tbody.get(i);
			Elements td = element1.select("tr>td");
			//System.out.println(td);

			tenantBetVo.setTenantId(1L);
			//数字多少没有关系
			tenantBetVo.setGameId(1L);
			tenantBetVo.setLossAmount(new BigDecimal(td.get(4).text()));
			//System.out.println("element1 = " + tenantBetVo);
		//	System.out.println("select = " + element);
		//	System.out.println(tenantBetVo);    List<T> Arrays.asList(map.values().toArray()
			List<TenanteBetBO> list=(List<TenanteBetBO>) Arrays.asList(tenantBetVo);
		//	System.out.println(list);
			if (tenantBetVo.getLossAmount().intValue() !=0){
				betDetailBO.setTenantBets(list);
			}
	}

		System.out.println(betDetailBO);

	}*/

}


