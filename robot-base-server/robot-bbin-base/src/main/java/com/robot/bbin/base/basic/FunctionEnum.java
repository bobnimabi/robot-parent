package com.robot.bbin.base.basic;


import com.robot.core.function.base.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 */
public enum FunctionEnum implements IFunctionEnum {
    BET_AMOUNT_AND_RECHARGE_SERVER("betAmountAndRechargeServer"),
    BREAK_AND_BET_SERVER("breakAndBetServer"),
    POMPON_AND_BETSERVER("pomponAndBetServer"),
    XBB_BREAK_AND_BET_SERVER("xbbBreakAndBetServer"),
    BREAK_SERVER("breakerServer"),

    ORDER_QUERY_SERVER("orderQueryServer"),
    LUCKY_NO_SERVER("luckyNoServer"),
    GAME_BET_SERVER("gameBetServer"),
    Query_User_Server("queryUserServer"),
    QUERY_RECHARGE_SERVER("queryRechargeServer"),
    //新亏损查询下注充值和亏损
    BET_AMOUNT_RECHARGE_LOSS_SERVER("betAmountRechargeLossServer"),
    PAY_SERVER("payServer"),

    LOGIN_SERVER("loginServer"),
    ;


    private final String serverName;

    private FunctionEnum(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getName() {
        return this.serverName;
    }
}
