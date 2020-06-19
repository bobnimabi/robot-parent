package com.robot.bbin.base.basic;


import com.robot.core.function.base.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 */
public enum FunctionEnum implements IFunctionEnum {
    BET_AMOUNT_AND_RECHARGE_SERVER("betAmountAndRechargeServer"),
    BREAK_AND_BET_SERVER("breakAndBetServer"),
    BREAK_SERVER("breakerServer"),
    ORDER_QUERY_SERVER("orderQueryServer"),
    GAME_BET_SERVER("gameBetServer"),
    Query_User_Server("queryUserServer"),
    QUERY_RECHARGE_SERVER("queryRechargeServer"),
    PAY_SERVER("payServer"),

    LOGIN_SERVER("loginServer"),
    ;

    private String serverName;

    private FunctionEnum(String serverName) {
        this.serverName = serverName;
    }


    @Override
    public String getName() {
        return this.serverName;
    }
}
