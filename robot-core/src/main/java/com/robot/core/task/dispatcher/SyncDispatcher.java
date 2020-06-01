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
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

/**
 * @Author mrt
 * @Date 2020/5/28 11:44
 * @Version 2.0
 */
@Service
public class SyncDispatcher extends AbstractDispatcher implements ISyncDispatcher{
    /**
     * 获取机器人的时长,单位：秒
     */
    private static final Duration PERIOD = Duration.ofSeconds(3);

    @Override
    public Response dispatch(ParamWrapper paramWrapper, IFunctionEnum functionEnum) throws Exception {
        Assert.notNull(paramWrapper,"paramWrapper不能为null");
        RobotWrapper robot = super.dispatcherFacde.getCookieDuration(PERIOD);
        if (null == robot || null == robot.getCookieStore()) {
            return Response.FAIL("机器人正忙或全部掉线");
        }
        try {
            return super.dispatch(paramWrapper, functionEnum, robot);
        }finally {
            super.dispatcherFacde.giveBackCookieAndToken(robot);
        }
    }

    @Override
    public Response disPatcherSpec(ParamWrapper paramWrapper, IFunctionEnum functionEnum, long robotId, boolean isNewCookie) throws Exception {
        Assert.notNull(paramWrapper,"paramWrapper不能为null");
        if (isNewCookie) {
            super.dispatcherFacde.newCookie(robotId);
        }
        RobotWrapper robot = super.dispatcherFacde.getCookie(robotId);
        if (null == robot || null == robot.getCookieStore()) {
            return Response.FAIL("指定机器人获取失败，robotId：" + robotId);
        }
        Response response = null;
        try {
            response = super.dispatch(paramWrapper, functionEnum, robot);
            return response;
        }finally {
            super.dispatcherFacde.giveBackCookie(robot);
            if (null  != response && response.isSuccess() && Response.LOGIN_SUCCESS_CODE == response.getCode()) {
                super.dispatcherFacde.online(robot);
            }
        }
    }
}
