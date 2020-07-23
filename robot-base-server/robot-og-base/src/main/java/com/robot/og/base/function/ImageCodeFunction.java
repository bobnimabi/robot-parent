package com.robot.og.base.function;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.http.HttpEntity;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.ICheckLost;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 付款
 */
@Slf4j
@Service
public class ImageCodeFunction extends AbstractFunction<Object,String, byte[]>  {
    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.IMAGE_CODE;
    }


    @Override
    protected IEntity getEntity(Object object, RobotWrapper robotWrapper) {
        return UrlEntity.custom(1)
                .add("type","agent") //
                .add("t",""+Math.random())
                ;
    }

/**
     * 注意：与登录相关的接口，返回null，不进行掉线检查
     */

    @Override
    protected ICheckLost getCHECK_LOST_SERVICE() {
        return null;
    }


    @Override
    protected IResultHandler<String, byte[]> getResultHandler() {
        return ResultHandler.INSTANCE;
    }


/**
     * 响应转换
     *
     */

    private static final class ResultHandler implements IResultHandler<String, byte[]> {

        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, byte[]> shr) throws IOException {
            Object result = shr.getOriginalEntity();
            log.info("图片验证码功能响应：{}", result);

            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("图片验证码未响应：" + result);
            }


            return Response.SUCCESS(result);
        }
    }



}
