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
import com.robot.og.base.bo.TenantBetDetailBO;
import com.robot.og.base.bo.TenanteBetBO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
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
public class GetBetDetailFunction extends AbstractFunction<BetQueryDto, String, TenantBetDetailBO> {

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
				.add("lastDate", DateUtils.format(dto.getEndDate()));
		List<String> gameList = dto.getGameList();
		for (String s : gameList) {
			entity.add("plat", s);
		}

		return entity;
	}

	@Override
	protected IResultHandler<String, TenantBetDetailBO> getResultHandler() {
		return ResultHandler.INSTANCE;
	}

	/**
	 * 响应结果转换：
	 * 存在返回：
	 */
	private static final class ResultHandler implements IResultHandler<String, TenantBetDetailBO> {
		private static final ResultHandler INSTANCE = new ResultHandler();

		private ResultHandler() {
		}

		@Override
		public Response parse2Obj(StanderHttpResponse<String, TenantBetDetailBO> shr) {
			String result = shr.getOriginalEntity();
			log.info("查询下注详情功能响应：{}");
			if (StringUtils.isEmpty(result)) {
				return Response.FAIL("未查询到下注记录");
			}
			Document doc = Jsoup.parse(result);
			Elements tds = doc.select("tbody>tr>td");
			Elements tbody = doc.select("tbody");
			Element element = tbody.get(0);
			Elements select = element.select("tbody>tr>td");
				//解析得到总下注
			String totalbett = select.get(3).text();

			TenantBetDetailBO tenantBetDetailBO = new TenantBetDetailBO();

			tenantBetDetailBO.setBalance(new BigDecimal(0));
			tenantBetDetailBO.setTotalBet(new BigDecimal(totalbett));

			tenantBetDetailBO.setTotalLoss(new BigDecimal(tds.get(4).text()));


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
					tenantBetDetailBO.setTenantBets(list);
				}

			}

			return Response.SUCCESS(tenantBetDetailBO);
		}
	}

/*	public static void main(String[] args) throws IOException {

		Document doc= Jsoup.parse(new File("test.html"), "utf-8");

		Elements tds = doc.select("tbody>tr>td");
		Elements tbody = doc.select("tbody");

		Element element = tbody.get(0);
		Elements select = element.select("tbody>tr>td");

		String totalbett = select.get(3).text();


		TenantBetDetailBO tenantBetDetailBO = new TenantBetDetailBO();

		tenantBetDetailBO.setBalance(new BigDecimal(0));
		tenantBetDetailBO.setTotalBet(new BigDecimal(tds.get(4).text()));

		tenantBetDetailBO.setTotalLoss(new BigDecimal(tds.get(4).text()));


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
				tenantBetDetailBO.setTenantBets(list);
			}

		}

	}*/


}
