package com.robot.gpk.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.ao.OrderQueryAO;
import com.robot.gpk.base.basic.PathEnum;
import com.robot.gpk.base.bo.OrderQueryBO;
import com.robot.gpk.base.bo.QueryNoBO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 局查询：注单查询
 * 局查询页面：
 *      请选择：注单编码
 * @Author tanke
 * @Date 2020/8/4 14:58
 * @Version 2.0
 */
@Service
public class OrderQueryFunction extends AbstractFunction<OrderQueryAO, String, OrderQueryBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.ORDER_QUERY;
    }

    @Override
    protected IEntity getEntity(OrderQueryAO ao, RobotWrapper robotWrapper) {
        return   JsonEntity.custom(7)
                .add("Account",ao.getAccount())
                .add("WagersTimeBegin",ao.getWagersTimeBegin())
                .add("WagersTimeEnd",ao.getWagersTimeEnd())
                .add("PayoffTimeEnd",ao.getPayoffTimeEnd())
                .add("GameCategories",ao.getGameCategories())
                .add("RawWagersId",ao.getRawWagersId())
                .add("connectionId", UUID.randomUUID().toString())
                ;
    }




    @Override
    protected IResultHandler<String, OrderQueryBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, OrderQueryBO> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, OrderQueryBO > shr) {
            String result = shr.getOriginalEntity();

            OrderQueryBO orderQueryBO = JSON.parseObject(result, OrderQueryBO.class);

            if(orderQueryBO.getPageData().size()==0){
                return Response.FAIL("会员账号或主单号有误,请核实后再申请哦!");
            }else if(orderQueryBO.getPageData().get(0).getBetAmount()<3){
                return Response.FAIL("下注金额小于3元不符合申请条件");

            }else {
                return Response.SUCCESS(orderQueryBO);
            }

        }
    }

}
