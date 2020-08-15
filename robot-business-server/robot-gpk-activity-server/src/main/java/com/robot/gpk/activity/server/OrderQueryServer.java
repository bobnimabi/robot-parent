package com.robot.gpk.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.util.DateUtils;
import com.robot.center.util.DateUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.ao.OrderQueryAO;
import com.robot.gpk.base.bo.OrderQueryBO;
import com.robot.gpk.base.bo.QueryNoBO;
import com.robot.gpk.base.function.OrderQueryFunction;
import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
import jdk.nashorn.internal.ir.WhileNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 局查询（所有平台均可使用）
 * 使用：查询注单
 * 注意：局查询页面必须是：
 *      请选择：注单编码
 * @Author mrt
 * @Date 2020/6/2 16:34
 * @Version 2.0
 *
 */
@Service
public class OrderQueryServer implements IAssemFunction<OrderNoQueryDTO> {



    @Autowired
    private OrderQueryFunction orderQueryFunction;



    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        Response<OrderQueryBO> queryNoBOResult = orderQueryFunction.doFunction(juQueryAOParams(queryDTO), robotWrapper);
        if(!queryNoBOResult.isSuccess()){
            return Response.FAIL("不符合申请条件");
        }
        OrderQueryBO orderQueryBO = queryNoBOResult.getObj();

        QueryNoBO queryNoBO = new QueryNoBO();
        queryNoBO.setPlatFormOrderNo(queryDTO.getOrderNo());
        queryNoBO.setUserName(queryDTO.getUserName());
        queryNoBO.setRebateAmount(String.valueOf(orderQueryBO.getPageData().get(0).getBetAmount()));
        queryNoBO.setOrderTime(orderQueryBO.getPageData().get(0).getPayoffTime());


      /*  if (DateUtils.format(queryNoBO.getOrderTime()).isBefore(queryDTO.getStartDate()) ) {  //
            return Response.FAIL("订单已过期,订单号："+queryDTO.getOrderNo());
        }    校验时间
*/
        return   Response.SUCCESS(queryNoBO);

    }
    /**
     * 查询 局查询参数组装
     */
    private ParamWrapper<OrderQueryAO>  juQueryAOParams(OrderNoQueryDTO queryDTO) {
        OrderQueryAO orderQueryAO = new OrderQueryAO();
        orderQueryAO.setAccount(queryDTO.getUserName());
        orderQueryAO.setWagersTimeBegin(  queryDTO.getStartDate());
        orderQueryAO.setWagersTimeEnd(queryDTO.getEndDate());
        orderQueryAO.setPayoffTimeEnd(queryDTO.getEndDate());
        orderQueryAO.setRawWagersId(queryDTO.getOrderNo());
        orderQueryAO.setGameCategories(Collections.singletonList("BBINprobability"));
        orderQueryAO.setConnectionId(UUID.randomUUID().toString());

        return new ParamWrapper<OrderQueryAO>(orderQueryAO);


    }


    public static void main(String[] args) {
        Collection arraylisr = new ArrayList();
        arraylisr.add(1);
        arraylisr.add(223.34);
        arraylisr.add("str");

        arraylisr.size();
        arraylisr.clear();
        arraylisr.contains(1);
        arraylisr.remove(1);
        arraylisr.isEmpty();
        Object[] objects = arraylisr.toArray();
        for (int i = 0; i < objects.length; i++) {

            Object object = objects[i];

            System.out.println("object = " + object);
        }
        Iterator iterator = arraylisr.iterator();

    while (iterator.hasNext()){
        Object next = iterator.next();
    }

    }


}
