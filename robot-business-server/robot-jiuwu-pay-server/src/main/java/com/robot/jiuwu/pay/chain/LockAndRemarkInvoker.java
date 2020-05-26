package com.robot.jiuwu.pay.chain;

import com.robot.center.chain.FilterChainBase;
import com.robot.center.function.ParamWrapper;
import com.robot.code.entity.TenantWithdrawRule;
import com.robot.code.service.ITenantWithdrawRuleService;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import com.robot.jiuwu.pay.function.LockAndRemarkServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mrt on 2020/1/9 0009 19:48
 * 锁单并备注并发送
 */
@Slf4j
@Service
public class LockAndRemarkInvoker extends FilterChainBase.Invoker<WithdrawListRowsData> {

    @Autowired
    private LockAndRemarkServer lockAndRemarkServer;

    @Override
    protected void before(int pos, List<WithdrawListRowsData> rows) throws Exception {
        log.info("==================锁单并备注==================");
        lockAndRemarkServer.doFunction(new ParamWrapper<List<WithdrawListRowsData>>(rows));
    }

    @Override
    protected void after(int pos, List<WithdrawListRowsData> rows) {

    }


}
