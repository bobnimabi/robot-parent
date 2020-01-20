package com.robot.jiuwu.pay.function;

import com.bbin.common.response.ResponseResult;
import com.robot.center.chain.FilterChain;
import com.robot.center.execute.IActionEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.dto.WithdrawListDTO;
import com.robot.jiuwu.base.function.DoLockServer;
import com.robot.jiuwu.base.function.UpdateRemark2Server;
import com.robot.jiuwu.base.function.WithdrawListServer;
import com.robot.jiuwu.base.vo.WithdrawListResultVO;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 抓取出款信息,并锁定
 */
@Slf4j
@Service
public class WithdrawServer extends FunctionBase<WithdrawListDTO> {
    @Autowired
    private WithdrawListServer withdrawListServer;
    @Autowired
    private FilterChain applicationChainFilter;

    @Override
    public ResponseResult doFunctionFinal(ParamWrapper<WithdrawListDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        // 获取审核返水列表
        ResponseResult listResponse = withdrawListServer.doFunction(paramWrapper, robotWrapper);
        if (!listResponse.isSuccess()) {
            return listResponse;
        }
        WithdrawListResultVO entity = (WithdrawListResultVO) listResponse.getObj();
        List<WithdrawListRowsData> rows = entity.getData().getRows();
        applicationChainFilter.doChain(rows);
        return ResponseResult.SUCCESS();
    }



    @Override
    public IActionEnum getActionEnum() {
        return null;
    }
}
