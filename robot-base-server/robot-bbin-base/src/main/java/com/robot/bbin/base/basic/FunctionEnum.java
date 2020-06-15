package com.robot.bbin.base.basic;


import com.robot.core.function.base.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 */
public enum FunctionEnum implements IFunctionEnum {
    BREAK_SERVER("breakerServer"),
    JU_QUERY_SERVER("juQueryServer"),
    LOGIN_IN_SERVER("loginInServer"),
    LUCK_ORDER_SERVER("luckOrderServer"),
    QUERY_BALANCE_SERVER("queryBalanceServer"),
    PAY_SERVER("payServer"),
    BET_SERVER("betServer"),
    BET_DETAIL_SERVER("betDetailServer"),
    IN_OUT_CASH_SERVER("inOutCashServer"),
    BET_AMOUNT_AND_RECHARGE_SERVER("betAmountAndRechargeServer"),
    ;
    /**
     * 服务名称
     */
    private String serverName;

    private FunctionEnum(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getName() {
        return this.serverName;
    }
}
