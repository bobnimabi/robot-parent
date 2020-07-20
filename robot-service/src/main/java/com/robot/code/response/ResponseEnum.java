package com.robot.code.response;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 */
public enum ResponseEnum {
    /**
     * 成功状态
     */
    GENERAL_SUCCESS(true, 10000, "操作成功"),
    LOGIN_SUCCESS(true, 10001, "登录成功"),
   OG_LOGIN_SUCCESS(true, 1000, "登录成功"),


    /**
     * 失败状态
     */
    GENERAL_FAILER(false, 11111, "操作失败"),
    LOST(false, 11112, "机器人掉线"),
    ;

    private boolean success;
    private int code;
    private String message;

    private ResponseEnum(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }
}
