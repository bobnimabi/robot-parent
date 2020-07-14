package com.robot.og.base.function;


import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 付款
 */
@Slf4j
@Service
public class ImageCodeFunction extends AbstractFunction<Object,String, String> {



    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.IMAGE_CODE;
    }


    @Override
    protected IEntity getEntity(Object object, RobotWrapper robotWrapper) {
        return UrlEntity.custom(1)
                .add("t", UUID.randomUUID().toString().replace("-", "")) // 0人工充值 1线上补单 2活动彩金 3补单 6其他
                ;
    }

    @Override
    protected IResultHandler<String, String> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应转换
     * 登录响应：
     *
     */
    private static final class ResultHandler implements IResultHandler<String, String> {

        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, String> shr) {
            String result = shr.getOriginalEntity();
            log.info("图片验证码功能响应：{}", result);

            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("图片验证码未响应：" + result);
            }
            return Response.SUCCESS(result);
        }
    }

}
