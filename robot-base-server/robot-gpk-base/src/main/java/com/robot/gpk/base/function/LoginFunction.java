package com.robot.gpk.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.code.dto.LoginDTO;
import com.robot.code.response.Response;
import com.robot.code.response.ResponseEnum;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.ICheckLost;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.basic.PathEnum;
import com.robot.gpk.base.bo.GeneralBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 登录
 */
@Slf4j
@Service
public class LoginFunction extends AbstractFunction<LoginDTO,String,Object> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.LOGIN;
    }

    @Override
    protected IEntity getEntity(LoginDTO params, RobotWrapper robotWrapper) {

        return JsonEntity.custom(2)
                .add("account", robotWrapper.getPlatformAccount())
                .add("password", robotWrapper.getPlatformPassword())
                ;
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

    /**
     * 响应转换
     * 登录响应：
     *  {"IsSuccess": true,"Methods": null}
     *  {"ReturnObject": null,"IsSuccess": false,"ErrorMessage": "您已经登入，无需再次登入"}
     */
    private static final class ResultHandler implements IResultHandler<String,Object>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, Object> shr) {
            String result = shr.getOriginalEntity();
            log.info("登录功能响应：{}", result);
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("未响应");
            }
            GeneralBO loginResultVo = JSON.parseObject(result, GeneralBO.class);
            if (!loginResultVo.getIsSuccess()) {
                return Response.FAIL(loginResultVo.getErrorMessage());
            }
            // 登录的最后一个接口成功后，要使用登录成功标记
            return Response.SUCCESS(ResponseEnum.LOGIN_SUCCESS);
        }
    }

}
