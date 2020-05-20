package com.robot.core.function.base;

import com.robot.core.function.base.IActionEnum;

public enum CommonActionEnum implements IActionEnum {
    FLUSH_SESSION("flush_session","刷新Session"),
    LOGIN("login","登录"),
    IMAGE_CODE("image_code","图片验证码"),
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
