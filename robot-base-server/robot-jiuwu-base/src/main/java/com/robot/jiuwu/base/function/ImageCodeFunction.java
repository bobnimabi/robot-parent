package com.robot.jiuwu.base.function;

import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.ICheckLost;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/2
 */


@Slf4j
@Service
public class ImageCodeFunction extends AbstractFunction<Object,String,Object> {
	@Override
	protected IPathEnum getPathEnum() {
		return PathEnum.IMAGE_CODE;
	}

	@Override
	protected IEntity getEntity(Object params, RobotWrapper robotWrapper) {
		return UrlEntity.custom(2);
		//todo
	}

	@Override
	protected IResultHandler getResultHandler() {
		return ResultHandler.INSTANCE;
	}

	/**
	 * 注意：与登录相关的接口，返回null，不进行掉线检查
	 */
	@Override
	protected ICheckLost getCHECK_LOST_SERVICE() {
		return null;
	}


	private static final class ResultHandler implements IResultHandler<String, Object> {
		private static final ResultHandler INSTANCE = new ResultHandler();

		private ResultHandler() {
		}


		@Override
		public Response parse2Obj(StanderHttpResponse<String, Object> shr) {

			String result = shr.getOriginalEntity();
			log.info("获取图片验证码响应：{}", result);


			if (StringUtils.isEmpty(result)) {
				return Response.FAIL("未响应");
			}
			// todo    解析并转换响应结果
			return null;
		}
	}
}