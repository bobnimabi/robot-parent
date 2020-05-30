package com.robot.core.task.dispatcher;

import com.robot.code.dto.Response;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.ParamWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author mrt
 * @Date 2020/5/29 19:14
 * @Version 2.0
 */
public class Dispatcher implements IDispatcher {
    @Autowired
    private IAsyncDispatcher asyncDispatcher;

    @Autowired
    private ISyncDispatcher syncDispatcher;

    @Override
    public void asyncDispatch(ParamWrapper paramWrapper, String exteralNo, IPathEnum actionEnum, IFunctionEnum functionEnum) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        asyncDispatcher.asyncDispatch(paramWrapper, exteralNo, actionEnum, functionEnum);
    }

    @Override
    public Response dispatch(ParamWrapper paramWrapper, IFunctionEnum functionEnum) throws Exception {
        return syncDispatcher.dispatch(paramWrapper,functionEnum);
    }

    @Override
    public Response disPatcherSpec(ParamWrapper paramWrapper, IFunctionEnum functionEnum, long robotId, boolean isNewCookie) throws Exception {
        return syncDispatcher.disPatcherSpec(paramWrapper, functionEnum, robotId, isNewCookie);
    }
}
