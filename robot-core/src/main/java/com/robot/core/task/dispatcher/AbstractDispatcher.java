package com.robot.core.task.dispatcher;

import com.robot.code.dto.Response;
import com.robot.core.function.base.IFunction;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.IDispatcherFacde;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @Author mrt
 * @Date 2020/5/28 11:47
 * @Version 2.0
 */
public abstract class AbstractDispatcher {

    @Autowired
    private Map<String, IFunction> functionMap;

    @Autowired
    protected IDispatcherFacde dispatcherFacde;

    /**
     * 调用Function执行功能，并返回机器人
     *
     * @param paramWrapper
     * @param functionEnum
     * @param robotWrapper
     * @return
     * @throws Exception
     */
    protected final Response dispatch(ParamWrapper paramWrapper, IFunctionEnum functionEnum, RobotWrapper robotWrapper) throws Exception {
        IFunction iFunction = getFunction(functionEnum);
        return iFunction.doFunction(paramWrapper, robotWrapper);
    }


    /**
     * 获取Function
     *
     * @param functionEnum
     * @return
     */
    IFunction getFunction(IFunctionEnum functionEnum) {
        IFunction iFunction = functionMap.get(functionEnum.getName());
        if (null == iFunction) {
            throw new IllegalArgumentException("Dispatcher：未获取到Function，请管理员检查");
        }
        return iFunction;
    }
}
