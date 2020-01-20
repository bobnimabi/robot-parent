package com.robot.jiuwu.pay.chain;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.chain.FilterChainBase;
import com.robot.center.constant.RobotConsts;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.code.entity.TenantWithdrawRule;
import com.robot.code.service.ITenantWithdrawRuleService;
import com.robot.jiuwu.base.dto.DoLockDTO;
import com.robot.jiuwu.base.dto.UpdateRemark2DTO;
import com.robot.jiuwu.base.function.DoLockServer;
import com.robot.jiuwu.base.function.UpdateRemark2Server;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import com.robot.jiuwu.pay.function.LockAndRemarkServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
