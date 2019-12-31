package com.robot.jiuwu.login.basic;

import com.robot.center.function.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 * 注意：登录是不需要的，登录在keepAlive里面直接调用Function
 */
public enum FunctionEnum implements IFunctionEnum {
    Image_CODE_SERVER("imageCodeServer"),
    QUERY_TOTAL_RECHARGE_SERVER("queryTotalRechargeServer"),
    QUERY_USER_SERVER("queryUserServer"),
    PAY_SERVER("payServer"),
    ;

    private String serverName;

    private FunctionEnum(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getFunctionServer() {
        return this.serverName;
    }
}
