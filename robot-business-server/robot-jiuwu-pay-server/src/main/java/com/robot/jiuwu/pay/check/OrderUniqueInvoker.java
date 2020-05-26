package com.robot.jiuwu.pay.check;

import com.alibaba.fastjson.JSON;
import com.robot.center.chain.FilterChainBase;
import com.robot.center.constant.RobotConsts;
import com.robot.center.tenant.TContext;
import com.robot.code.service.ITenantWithdrawRuleService;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mrt on 2020/1/9 0009 19:48
 * 过滤重复订单
 */
@Slf4j
@Service
public class OrderUniqueInvoker extends FilterChainBase.Invoker<WithdrawListRowsData> {
    @Autowired
    private StringRedisTemplate redis;

    @Override
    protected void before(int pos, List<WithdrawListRowsData> rows) {
        log.info("==================订单唯一检查==================");
        Iterator<WithdrawListRowsData> iterator = rows.iterator();
        while (iterator.hasNext()) {
            WithdrawListRowsData row = iterator.next();
            String cacheUniqueOrder = createCacheUniqueOrder(String.valueOf(row.getId()));
            Boolean hasKey = redis.hasKey(cacheUniqueOrder);
            if (hasKey) {
                iterator.remove();
                log.info("订单唯一限制，去除：{}", JSON.toJSONString(row));
            }
        }
    }

    @Override
    protected void after(int pos, List<WithdrawListRowsData> rows) {
        for (WithdrawListRowsData row : rows) {
            String cacheUniqueOrder = createCacheUniqueOrder(String.valueOf(row.getId()));
            redis.opsForValue().set(cacheUniqueOrder, "", Duration.ofDays(30));
        }
    }

    private static String createCacheUniqueOrder(String orderId) {
        return new StringBuilder(60)
                .append(RobotConsts.WITHDRAW_UNIQUE_ORDER_NO)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(orderId)
                .toString();
    }
}
