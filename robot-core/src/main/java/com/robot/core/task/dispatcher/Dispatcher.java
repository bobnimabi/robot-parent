package com.robot.core.task.dispatcher;

import com.robot.code.dto.LoginDTO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.IActionEnum;
import com.robot.core.function.base.IFunction;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void asyncDispatch(ParamWrapper paramWrapper, String outNo, String waitField, Duration waitTime, IActionEnum actionEnum, IFunctionEnum functionEnum) {
        int robotLimitInterval = super.dispatcherFacde.getRobotLimitInterval(actionEnum);
        taskPool.taskAdd(new TaskWrapper(paramWrapper, functionEnum, waitField, waitTime, actionEnum.getActionCode(), Duration.ofSeconds(robotLimitInterval)), outNo);
    }
}
