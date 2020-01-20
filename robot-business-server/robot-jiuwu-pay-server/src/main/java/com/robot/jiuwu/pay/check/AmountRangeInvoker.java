package com.robot.jiuwu.pay.check;

import com.alibaba.fastjson.JSON;
import com.robot.center.chain.FilterChainBase;
import com.robot.center.util.MoneyUtil;
import com.robot.code.entity.TenantWithdrawRule;
import com.robot.code.service.ITenantWithdrawRuleService;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mrt on 2020/1/10 0010 12:14
 */
@Slf4j
@Service
public class AmountRangeInvoker extends FilterChainBase.Invoker<WithdrawListRowsData>{

    @Autowired
    private ITenantWithdrawRuleService ruleService;

    @Override
    protected void before(int pos, List<WithdrawListRowsData> rows) {
        log.info("==================金额检查==================");
        TenantWithdrawRule rule = ruleService.getOne(null);
        log.info("金额范围:{}-{}", rule.getMinAmount(), rule.getMaxAmount());
        Iterator<WithdrawListRowsData> iterator = rows.iterator();
        while (iterator.hasNext()){
            WithdrawListRowsData row = iterator.next();
            BigDecimal money = row.getMoney();
            if (money.compareTo(rule.getMinAmount()) < 0 || money.compareTo(rule.getMaxAmount()) > 0) {
                iterator.remove();
                log.info("金额超出范围:去除：{}", JSON.toJSONString(row));
            }
        }
    }

    @Override
    protected void after(int pos, List<WithdrawListRowsData> rows) {

    }
}
