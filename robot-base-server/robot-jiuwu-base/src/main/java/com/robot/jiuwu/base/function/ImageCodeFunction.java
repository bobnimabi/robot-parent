package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
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
import com.robot.jiuwu.base.vo.ImageCodeResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *获取图片验证码
 * </p>
 *
 * @author tanke
 * @date 2020/7/2
 */


@Slf4j
@Service
public class ImageCodeFunction extends AbstractFunction<Object,String,ImageCodeResultVO> {
	@Override
	protected IPathEnum getPathEnum() {
		return PathEnum.IMAGE_CODE;
	}

	@Override
	protected IEntity getEntity(Object params, RobotWrapper robotWrapper) {
		return UrlEntity.custom(0);

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


	private static final class ResultHandler implements IResultHandler<String, ImageCodeResultVO> {
		private static final ResultHandler INSTANCE = new ResultHandler();

		private ResultHandler() {
		}


		@Override
		public Response parse2Obj(StanderHttpResponse<String, ImageCodeResultVO> shr) {

			String result = shr.getOriginalEntity();

			if (StringUtils.isEmpty(result)) {
				return Response.FAIL("图片验证码未响应");
			}
			ImageCodeResultVO imageCodeResult = JSON.parseObject(result, ImageCodeResultVO.class);
			//需要修改
			if (null == imageCodeResult.getCode()) {
				return Response.FAIL("转换失败");
			}
			return Response.SUCCESS(imageCodeResult);
		}
	}

}