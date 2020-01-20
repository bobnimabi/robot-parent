package com.robot.jiuwu.pay.chain;

import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.pojo.TenantPayPlatformOrderDTO;
import com.bbin.common.pojo.TenantPayPlatformOrderVo;
import com.robot.center.chain.FilterChainBase;
import com.robot.center.function.ParamWrapper;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import com.robot.jiuwu.pay.function.LockAndRemarkServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 2020/1/9 0009 19:48
 * 锁单并备注并发送
 */
@Slf4j
@Service
public class SenderInvoker extends FilterChainBase.Invoker<WithdrawListRowsData> {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private LockAndRemarkServer lockAndRemarkServer;

    @Override
    protected void before(int pos, List<WithdrawListRowsData> rows) throws Exception {
        log.info("==================mq发送==================");
        List<TenantPayPlatformOrderDTO> list = new ArrayList<>();
        for (WithdrawListRowsData row:rows) {
            TenantPayPlatformOrderDTO orderDTO = new TenantPayPlatformOrderDTO();
            orderDTO.setCardName(row.getBank());
            orderDTO.setPaymentAmount(row.getMoney());
            orderDTO.setBankCity("未知");
            orderDTO.setBankProvince("未知");
            orderDTO.setBankName(row.getBank());
            orderDTO.setPlatformUserName(String.valueOf(row.getGameid()));
            orderDTO.setPlatUserId(String.valueOf(row.getId()));// 携带参数，回调是会携带回来
            orderDTO.setPlatformUserRealName(row.getName());
            orderDTO.setPlatformOrderNo(String.valueOf(row.getId()));
            orderDTO.setTagName(String.valueOf(row.getMemberOrder()));
            orderDTO.setPlatformStatus(1);
            boolean add = list.add(orderDTO);
            if (!add) {
                log.info("添加mq出款信息失败：{}", JSON.toJSONString(orderDTO));
            }
        }

        payToCard(list);
    }

    //调用支付微服务
    private void payToCard(List<TenantPayPlatformOrderDTO> list) throws Exception{
        // 调用支付微服务
        rabbitTemplate.convertSendAndReceiveAsType(RabbitMqConstants.PAY_AGENT_EXCHANGE_NAME, RabbitMqConstants.PAY_AGENT_ROUTE_KEY, list,new ParameterizedTypeReference<List<TenantPayPlatformOrderVo>>(){
            @Override
            public Type getType() {
                return super.getType();
            }
        });
    }

    @Override
    protected void after(int pos, List<WithdrawListRowsData> rows) {

    }


}
