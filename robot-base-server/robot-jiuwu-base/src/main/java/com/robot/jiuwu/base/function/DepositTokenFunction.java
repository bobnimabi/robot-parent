package com.robot.jiuwu.base.function;

import com.bbin.common.pojo.TaskAtomDto;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;

import com.robot.jiuwu.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 付款前：获取Token（防表单重复提交）   todo   没看见有路径 数据库中
 */
@Slf4j
@Service
public class DepositTokenFunction extends AbstractFunction<TaskAtomDto,String,String> {

    @Override
    protected IPathEnum getPathEnum() {
     // return PathEnum.;
        return null;  //
    }

    @Override
    protected IEntity getEntity(TaskAtomDto params, RobotWrapper robotWrapper) {
        return null;
    }

    @Override
    protected IResultHandler<String, String> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应转换
ss     */
    private static final class ResultHandler implements IResultHandler<String,String>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, String> shr) {
            String result = shr.getOriginalEntity();
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("DepositTokenFunction:未响应");
            }
            return Response.SUCCESS(result.substring(1,result.length()-1));
        }
    }
}
