package com.robot.suxiao.base.basic;

import com.robot.center.function.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 * 注意：登录是不需要的，登录在keepAlive里面直接调用Function
 */
public enum FunctionEnum implements IFunctionEnum {
    CANCEL_CARDS_ERVER("cancelCardServer"),
    MAIN_SERVER("mainServer"),
    QUERY_USER_INFO_SERVER("queryUserInfoServer"),
    WITHDRAW_FINAL_SERVER("withdrawFinalServer"),
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
