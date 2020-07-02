package com.robot.jiuwu.base.basic;


import com.robot.core.function.base.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 */
public enum FunctionEnum implements IFunctionEnum {
    LOGIN_SERVER("logInServer"),
    QUERY_USER_SERVER("queryUserServer"),
    PAY_SERVER("payServer"),
	Image_CODE_SERVER("imageCodeServer");

    private final String serverName;

    private FunctionEnum(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getName() {
        return this.serverName;
    }
}
