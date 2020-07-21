package com.robot.og.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.QueryRechargeAO;
import com.robot.og.base.basic.PathEnum;
import com.robot.og.base.dto.Convert;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询总充值
 */
@Slf4j
@Service
public class GetRechargeFunction extends AbstractFunction<QueryRechargeAO, String, String> {

	@Override
	protected IPathEnum getPathEnum() {
		return PathEnum.QUERY_RECORD;
	}

	@Override
	protected IEntity getEntity(QueryRechargeAO ao, RobotWrapper robotWrapper) {
		return UrlEntity.custom(11)
				.add("type", "queryRecord")
				.add("tradeTypes", "31,12,15,")
				.add("isPostback", "1")
				.add("orderField", "createDateTime")
				.add("sortBy", "DESC")
				.add("selDate", "0")
				.add("startDate", ao.getStartDate())
				.add("endDate", ao.getEndDate())
				.add("actType", "0")
				.add("memberNo", ao.getMemberNo()) //usename
				.add("pageSize", "20")
				;


	}

	@Override
	protected IResultHandler<String, String> getResultHandler() {
		return ResultHandler.INSTANCE;
	}

	/**
	 * 响应结果转换：
	 * 存在返回：
	 */
	private static final class ResultHandler implements IResultHandler<String, String> {
		private static final ResultHandler INSTANCE = new ResultHandler();

		private ResultHandler() {
		}

		@Override
		public Response parse2Obj(StanderHttpResponse<String, String> shr) {
			String result = shr.getOriginalEntity();
			log.info(" 查询结果响应   ");
			Document doc = Jsoup.parse(result);
			Elements th = doc.select("table[class=layui-table custom-table] thead tr");
			List<String> ths = Convert.parseThs(th.get(0));

			//  <table border="1" cellspacing="0" class="layui-table custom-table">
			Elements trs = doc.select("table[class=layui-table custom-table] tbody tr");

			// 移除最后3行
			for (int i = 0; i < 3; i++) {
				trs.remove(trs.size() - 1);
			}
			List<Map<String, String>> list = Convert.parseListMap(ths, trs);
			log.info("{}",list);
			if (list.size() == 0) {
				return Response.FAIL("未查询到充值信息");
			}

			return Response.SUCCESS(list);


		}
	}

}
