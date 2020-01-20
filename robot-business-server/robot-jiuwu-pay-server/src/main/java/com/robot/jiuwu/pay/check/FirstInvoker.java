package com.robot.jiuwu.pay.check;

import com.alibaba.fastjson.JSON;
import com.robot.center.chain.FilterChainBase;
import com.robot.code.entity.TenantWithdrawRule;
import com.robot.code.service.ITenantWithdrawRuleService;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mrt on 2020/1/10 0010 12:14
 * 首次出款不出
 */
@Slf4j
@Service
public class FirstInvoker extends FilterChainBase.Invoker<WithdrawListRowsData>{
    @Autowired
    private ITenantWithdrawRuleService ruleService;

    @Override
    protected void before(int pos, List<WithdrawListRowsData> rows) {
        log.info("==================首次出款检查==================");
        TenantWithdrawRule rule = ruleService.getOne(null);
        if (rule.getIsLimitFirst()) {
            Iterator<WithdrawListRowsData> iterator = rows.iterator();
            while (iterator.hasNext()){
                WithdrawListRowsData row = iterator.next();
                if (null != row.getTotalWithdraw() && BigDecimal.ZERO.compareTo(row.getTotalWithdraw()) == 0) {
                    iterator.remove();
                    log.info("首次出款，去除：{}", JSON.toJSONString(row));
                }
            }
        }
    }

    @Override
    protected void after(int pos, List<WithdrawListRowsData> rows) {

    }
}
