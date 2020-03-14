package com.robot.xiaoka.income.function;

import bom.bbin.common.dto.income.robot.WithDrawDTO;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.suxiao.base.function.QueryUserInfoServer;
import com.robot.suxiao.base.function.WithdrawServer;
import com.robot.suxiao.base.vo.BankInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询银行卡结果
 */
@Slf4j
@Service
public class WithdrawFinalServer extends FunctionBase<WithDrawDTO> {

    @Autowired
    private QueryUserInfoServer queryUserInfoServer;

    @Autowired
    private WithdrawServer withdrawServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<WithDrawDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        ResponseResult userInfoResult = queryUserInfoServer.doFunction(paramWrapper, robotWrapper);
        if (!userInfoResult.isSuccess()) {
            return userInfoResult;
        }
        BankInfoVO bankInfoVO = (BankInfoVO) userInfoResult.getObj();
        return withdrawServer.doFunction(new ParamWrapper<BankInfoVO>(bankInfoVO), robotWrapper);
    }

    @Override
    public IActionEnum getActionEnum() {
        return null;
    }
}

