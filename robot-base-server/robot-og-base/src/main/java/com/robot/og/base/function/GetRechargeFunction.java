package com.robot.og.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.dto.robot.BreakThroughDTO;
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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询总充值
 */
@Slf4j
@Service
public class GetRechargeFunction extends AbstractFunction<BreakThroughDTO, String, String> {

	@Override
	protected IPathEnum getPathEnum() {
		return PathEnum.QUERY_RECORD;
	}

	@Override
	protected IEntity getEntity(BreakThroughDTO ao, RobotWrapper robotWrapper) {
		return UrlEntity.custom(11)
				.add("type", "queryRecord")
				.add("tradeTypes", "31,12,15,")
				.add("isPostback", "1")
				.add("orderField", "createDateTime")
				.add("sortBy", "DESC")
				.add("selDate", "0")
				.add("startDate", ao.getBeginDate())
				.add("endDate", ao.getEndDate())
				.add("actType", "0")
				.add("memberNo", ao.getUserName())
				.add("pageSize", "50")
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

		/**
		 * 查询总充值
		 *
		 * @param shr
		 * @return
		 */

		@Override
		public Response parse2Obj(StanderHttpResponse<String, String> shr) {
			String result = shr.getOriginalEntity();
			if(StringUtils.isEmpty(result)){
				return Response.FAIL("未查询到充值金额");
			}
			log.info(" 查询结果响应   ");

			Document doc = Jsoup.parse(result);
			String totalRecharge = doc.getElementById("_countMoney").text();

			return Response.SUCCESS(totalRecharge);

		}
	}


}
