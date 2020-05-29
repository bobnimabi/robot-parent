package com.robot.core.task.dispatcher;

import com.robot.code.dto.Response;
import com.robot.code.entity.AsyncRequestConfig;
import com.robot.code.service.IAsyncRequestConfigService;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

/**
 * @Author mrt
 * @Date 2020/5/28 11:44
 * @Version 2.0
 */
@Service
public class Dispatcher extends AbstractDispatcher {

    @Autowired
    private ITaskPool taskPool;

    @Autowired
    private IAsyncRequestConfigService asyncConfig;

    /**
     * 获取机器人的时长
     */
    private static final Duration PERIOD = Duration.ofSeconds(3);

    @Override
    public Response dispatch(ParamWrapper paramWrapper, IFunctionEnum functionEnum) throws Exception {
        RobotWrapper syncRobot = super.dispatcherFacde.getRobotDuration(PERIOD);
        if (null == syncRobot) {
            return Response.FAIL("机器人正忙或全部掉线");
        }
        return super.dispatch(paramWrapper, functionEnum, syncRobot);
    }

    @Override
    public Response disPatcherSpec(ParamWrapper paramWrapper, IFunctionEnum functionEnum, long robotId, boolean isNewCookie) throws Exception {
        if (isNewCookie) {
            super.dispatcherFacde.newCookie(robotId);
        }
        RobotWrapper syncRobot = super.dispatcherFacde.getCookie(robotId);
        if (null == syncRobot) {
            return Response.FAIL("指定机器人获取失败，robotId：" + robotId);
        }
        return super.dispatch(paramWrapper, functionEnum, syncRobot);
    }

    @Override
    public void asyncDispatch(ParamWrapper paramWrapper, String exteralNo, IPathEnum pathEnum, IFunctionEnum functionEnum) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        AsyncRequestConfig asyncRequestConfig = asyncConfig.get(pathEnum.getpathCode());
        taskPool.taskAdd(new TaskWrapper(paramWrapper, functionEnum, asyncRequestConfig), exteralNo);
    }
}
