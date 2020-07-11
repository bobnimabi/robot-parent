package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.basic.PathEnum;
import com.robot.jiuwu.base.vo.UserInfoDetailResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/3
 */
@Slf4j
@Service
public class QueryUserDetailFunction extends AbstractFunction<String,String,UserInfoDetailResultVO> {
	@Override
	protected IPathEnum getPathEnum() {
		return PathEnum.QUERY_USER_DETAIL;
	}

	@Override
	protected IEntity getEntity(String userId, RobotWrapper robotWrapper) {
		return JsonEntity.custom(1).add("gameid", userId);
	}

	@Override
	protected IResultHandler<String, UserInfoDetailResultVO> getResultHandler() {
		return ResultHandler.INSTANCE;
	}



	private static final class ResultHandler implements IResultHandler<String, UserInfoDetailResultVO>{
		private static final ResultHandler INSTANCE = new ResultHandler();
		private ResultHandler(){}

		//VIP等级  usesrResultVO.getData().getInfo().getMemberOrder()

		@Override
		public Response parse2Obj(StanderHttpResponse<String, UserInfoDetailResultVO> shr) {
			String result = shr.getOriginalEntity();
			log.info("查询会员存在功能响应：{}", result);
			if (StringUtils.isEmpty(result)) {
				return Response.FAIL("未响应");
			}

			UserInfoDetailResultVO usesrResultVO = JSON.parseObject(result, UserInfoDetailResultVO.class);
			if (null == usesrResultVO.getCode()) {
				return Response.FAIL("转换失败");
			}
			return Response.SUCCESS(usesrResultVO);

		}
	}
}
