package com.robot.gpk.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.basic.PathEnum;
import com.robot.gpk.base.bo.JuQueryBO;
import com.robot.gpk.base.bo.OrderPageData;
import com.robot.gpk.base.bo.OrderQueryBO;
import org.springframework.stereotype.Service;

/**
 * 局查询：注单查询
 * 局查询页面：
 *      请选择：注单编码
 * @Author mrt
 * @Date 2020/6/2 14:58
 * @Version 2.0
 */
@Service
public class OrderQueryFunction extends AbstractFunction<OrderNoQueryDTO, String, JuQueryBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.ORDER_QUERY;
    }

    @Override
    protected IEntity getEntity(OrderNoQueryDTO queryDTO, RobotWrapper robotWrapper) {
        return   JsonEntity.custom(7)
                .add("","")
                ;

    }




    @Override
    protected IResultHandler<String, JuQueryBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, JuQueryBO> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, JuQueryBO> shr) {
            String result = shr.getOriginalEntity();

            OrderQueryBO orderQueryBO = JSON.parseObject(result, OrderQueryBO.class);
            if (null== orderQueryBO.getPageData()){
                return Response.FAIL("会员账号或主单号有误");
            }

            return null;

        }
    }

}
