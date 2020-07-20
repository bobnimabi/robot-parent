package com.robot.og.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bbin.common.response.ResponseResult;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;


import com.robot.og.base.ao.QueryUserAO;
import com.robot.og.base.basic.PathEnum;
import com.robot.og.base.bo.QueryUserBO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户是否存在，和基本信息
 */
@Slf4j
@Service
public class QueryUserFunction extends AbstractFunction<QueryUserAO, String, QueryUserBO> {

	@Override
	protected IPathEnum getPathEnum() {
		return PathEnum.QUERY_USER;
	}

	@Override
	protected IEntity getEntity(QueryUserAO ao, RobotWrapper robotWrapper) {
		return UrlEntity.custom(1)
				.add("type", "queryManDeposit")
				.add("account", ao.getAccount())
				;
	}

	@Override
	protected IResultHandler<String, QueryUserBO> getResultHandler() {
		return ResultHandler.INSTANCE;
	}

	/**
	 * 响应结果转换：
	 */
	private static final class ResultHandler implements IResultHandler<String, QueryUserBO> {
		private static final ResultHandler INSTANCE = new ResultHandler();

		private ResultHandler() {
		}

		@Override
		public Response parse2Obj(StanderHttpResponse<String, QueryUserBO> shr) {
			String result = shr.getOriginalEntity();
			if(null==result){
				return Response.FAIL("result为空会员不存在");
			}
			log.info("查询会员存在功能响应：{}");
			Document doc = Jsoup.parse(result);
			//解析会员名称是否存在
			String memberId = doc.getElementById("memberId").attr("value");

			//解析账户余额额度
			Elements table = doc.getElementsByTag("table");
			Element table2 = table.get(1);
			Elements trs = table2.getElementsByTag("tr");
			Element tr3 = trs.get(2);
			Elements tds = tr3.getElementsByTag("td");
			Element td = tds.get(1);
			String balance = td.text();
			QueryUserBO queryUserBO = new QueryUserBO();
			queryUserBO.setMemberId(memberId);
			queryUserBO.setBalance(balance);

			if (StringUtils.isEmpty(queryUserBO.getMemberId()) || StringUtils.isEmpty(queryUserBO.getBalance())) {
				return Response.FAIL("会员不存在");
			}

			return Response.SUCCESS(queryUserBO);

		}
	}



}
