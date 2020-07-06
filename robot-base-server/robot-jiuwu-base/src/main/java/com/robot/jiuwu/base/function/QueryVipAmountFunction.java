package com.robot.jiuwu.base.function;

import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.robot.manager.RobotWrapper;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/3
 */
public class QueryVipAmountFunction  extends AbstractFunction<Object,String,Object> {
	@Override
	protected IPathEnum getPathEnum() {
		return null;
	}

	@Override
	protected IEntity getEntity(Object params, RobotWrapper robotWrapper) {
		return null;
	}

	@Override
	protected IResultHandler<String, Object> getResultHandler() {
		return null;
	}
}
