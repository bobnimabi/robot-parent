package com.robot.center.execute;

public enum CommonActionEnum implements IActionEnum {
    FLUSH_SESSION("flush_session","刷新Session"),
    LOGIN("login","登录"),
    ;
    private final String actionCode;
    private final String message;

    private CommonActionEnum(String actionCode, String message) {
        this.actionCode = actionCode;
        this.message = message;
    }

    @Override
    public String getActionCode() {
        return this.actionCode;
    }
}
