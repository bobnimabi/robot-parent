package com.robot.bbin.activity.basic;

import com.robot.center.function.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 */
public enum FunctionEnum implements IFunctionEnum {
    BREAK_SERVER("breakerServer"),
    JU_QUERY_SERVER("juQueryServer"),
    LOGIN_IN_SERVER("loginInServer"),
    LUCK_ORDER_SERVER("luckOrderServer"),
    QUERY_BALANCE_SERVER("queryBalanceServer"),
    PAY_SERVER("payServer");

    private String serverName;

    private FunctionEnum(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getFunctionServer() {
        return this.serverName;
    }
}
