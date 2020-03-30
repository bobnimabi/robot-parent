package com.robot.liantong.base.basic;

import com.robot.center.function.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 * 注意：
 *  1.登录不需要（）
 */
public enum FunctionEnum implements IFunctionEnum {
    LOGIN_CHECK_SERVER("loginCheckServer"),
    IS_LOGIN_SERVER("isLoginServer"),
    SMS_SERVER("smsServer"),

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