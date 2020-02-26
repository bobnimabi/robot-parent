package com.robot.jiuwu.pay.check;

import com.alibaba.fastjson.JSON;
import com.robot.center.chain.FilterChainBase;
import com.robot.center.constant.RobotConsts;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.code.entity.TenantWithdrawRule;
import com.robot.code.service.ITenantWithdrawRuleService;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by mrt on 2020/1/9 0009 19:48
 * 同一会员一段时间内不能再次出款
 */
@Slf4j
@Service
public class UserNameInvoker extends FilterChainBase.Invoker<WithdrawListRowsData> {

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ITenantWithdrawRuleService ruleService;

    @Override
    protected void before(int pos, List<WithdrawListRowsData> rows) {
        log.info("==================会员账号检查==================");
        Iterator<WithdrawListRowsData> iterator = rows.iterator();
        Set<Long> set = new HashSet<>();
        while (iterator.hasNext()) {
            WithdrawListRowsData row = iterator.next();
            // 重复去重
            boolean isAdd = set.add(row.getGameid());
            if (!isAdd) {
                iterator.remove();
                log.info("重复会员，去除：{}", JSON.toJSONString(row));
                continue;
            }
            // 检查userName标志
            Boolean hasKey = redis.hasKey(createCacheUniqueUserName(String.valueOf(row.getGameid())));
            if (hasKey) {
                iterator.remove();
                log.info("同会员时间限制，去除：{}", JSON.toJSONString(row));
            }
        }
    }

    @Override
    protected void after(int pos, List<WithdrawListRowsData> rows) {
        TenantWithdrawRule rule = ruleService.getOne(null);
        for (WithdrawListRowsData row : rows) {
            String cacheUniqueUserName = createCacheUniqueUserName(String.valueOf(row.getGameid()));
            redis.opsForValue().setIfAbsent(cacheUniqueUserName, "", Duration.ofMinutes(rule.getLimitMinuts()));
        }
    }

    private static String createCacheUniqueUserName(String userName) {
        return new StringBuilder(60)
                .append(RobotConsts.WITHDRAW_UNIQUE_USERNAME)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction()).append(":")
                .append(userName)
                .toString();
    }
}
