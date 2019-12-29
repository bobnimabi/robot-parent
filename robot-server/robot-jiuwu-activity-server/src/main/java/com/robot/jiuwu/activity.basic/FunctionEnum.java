package com.robot.jiuwu.activity.basic;

import com.robot.center.function.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 */
public enum FunctionEnum implements IFunctionEnum {
    Image_CODE_SERVER("imageCodeServer"),
    LOGIN_IN_SERVER("loginInServer"),
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
