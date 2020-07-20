package com.robot.core.task.dispatcher;

import com.alibaba.fastjson.JSON;
import com.robot.code.dto.LoginDTO;
import com.robot.code.response.Response;
import com.robot.code.response.ResponseEnum;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
            return handleResponse(super.dispatch(paramWrapper, functionEnum, robot));
        }finally {
            super.dispatcherFacde.giveBackCookieAndToken(robot);
        }
    }

    @Override
    public Response disPatcherLogin(ParamWrapper<LoginDTO> paramWrapper, IFunctionEnum functionEnum, boolean isNewCookie) throws Exception {
        Assert.notNull(paramWrapper,"paramWrapper不能为null");
        long robotId = paramWrapper.getObj().getId();
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
            if (null  != response && response.isSuccess() && ResponseEnum.LOGIN_SUCCESS.getCode() == response.getCode()) {    //|| ResponseEnum.OG_LOGIN_SUCCESS.getCode() ==response.getCode()
                super.dispatcherFacde.online(robot);
            }
        }
    }

    private Response handleResponse(Response response) {
        return response.setObj(JSON.toJSONString(response.getObj()));
    }
}
