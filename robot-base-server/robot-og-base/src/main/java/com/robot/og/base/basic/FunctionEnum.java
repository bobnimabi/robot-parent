package com.robot.og.base.basic;


import com.robot.core.function.base.IFunctionEnum;


/**
 * Created by mrt on 11/15/2019 8:27 PM
 */
public enum FunctionEnum implements IFunctionEnum {
    LOGIN_SERVER("loginServer"),
    QUERY_USER_SERVER("queryUserServer"),
    IMAGE_CODE_SERVER("imageCodeServer"),
    PAY_SERVER("payServer"),
    GETDETAIL_SERVER("getBetDetailServer"),
    GETTOTAL_AMOUNT_SERVER("getAmountServer"),
    GETRECHARGE_SERVER("getRechargeServer"),

    ORDER_QUERYNO_SERVER("orderQueryServer"),

    QUERY_ACCOUNT_SERVER("queryAccountServer"),

    QUERY_USERRECORD_SERVER("queryUserRecordServer"),
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






