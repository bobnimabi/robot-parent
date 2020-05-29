package com.robot.center.execute;

public enum CommonActionEnum implements IPathEnum {
    FLUSH_SESSION("flush_session","刷新Session"),
    LOGIN("login","登录"),
    IMAGE_CODE("image_code","图片验证码"),
    ;
    private final String pathCode;
    private final String message;

    private CommonActionEnum(String pathCode, String message) {
        this.pathCode = pathCode;
        this.message = message;
    }

    @Override
    public String getpathCode() {
        return this.pathCode;
    }
}
