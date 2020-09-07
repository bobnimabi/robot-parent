package com.robot.og.base.function;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbin.common.client.BetQueryDto;
import com.bbin.common.util.DateUtils;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.code.entity.VsGame;
import com.robot.og.base.basic.GameIdEnum;
import com.robot.og.base.basic.PathEnum;
import com.robot.og.base.bo.TenantBetDetailBO;
import com.robot.og.base.bo.TenanteBetBO;
import com.robot.og.base.common.Constant;
import com.robot.code.service.impl.VsGameServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tanke
 * 下注详情
 */
@Slf4j
@Service
public class GetBetDetailFunction extends AbstractFunction<BetQueryDto, String, TenantBetDetailBO> {

	@Autowired
	@Lazy
	private VsGameServiceImpl gameService;

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


		if (CollectionUtils.isEmpty(dto.getGameList())) {
			List<VsGame> gameList2 = gameService.list(new QueryWrapper<VsGame>().select("game_code").eq("status", Constant.YES));
			for (VsGame game : gameList2) {
				entity.add("plat", game.getGameCode());
			}
		} else {
			for (String game : dto.getGameList()) {
				entity.add("plat", game);
			}
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
				if(tbody.size()==0){
					return Response.FAIL("未查询到下注记录,请检查您的会员账号或下注记录");
				}
			Elements tby1 = tbody.get(0).select("tbody>tr>td");
			String totalbett = tby1.get(3).text();
			TenantBetDetailBO tenantBetDetailBO = new TenantBetDetailBO();
			tenantBetDetailBO.setBalance(new BigDecimal(0));
			tenantBetDetailBO.setTotalBet(new BigDecimal(totalbett));
			tenantBetDetailBO.setTotalLoss(new BigDecimal(tds.get(4).text()));
			ArrayList<TenanteBetBO> betBolist = new ArrayList<>();
			for (int i = 1; i < tbody.size(); i++) {
				TenanteBetBO tenantBetVo = new TenanteBetBO();
				Elements tdss = tbody.get(i).select("tr>td");
				tenantBetVo.setTenantId(1L);

				tenantBetVo.setLossAmount(new BigDecimal(tdss.get(4).text()));
				tenantBetVo.setBetAmount(new BigDecimal(tdss.get(3).text()));


				//获取游戏名称  根据游戏名称设置gameid
				Elements tdssa = tbody.get(i).select("tr>td>a");
				String onclick = tdssa.attr("onclick").substring(9);

				Pattern P= Pattern.compile("[A-Z]{4,}");
				Matcher m=P.matcher(onclick);
				String names=null;
				while (m.find()) {
					String name = m.group(0);


					if(null!=name){
						names=name;
						 tenantBetVo.setGameId(names);
					}
				}
				if (tenantBetVo.getBetAmount().compareTo(BigDecimal.ZERO)!=0)
				betBolist.add(tenantBetVo);
			}

			tenantBetDetailBO.setTenantBets(betBolist);
			return Response.SUCCESS(tenantBetDetailBO);
		}

	}


}
