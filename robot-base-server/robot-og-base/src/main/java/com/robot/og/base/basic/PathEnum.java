package com.robot.og.base.basic;

import com.robot.core.function.base.IPathEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 */
public enum PathEnum implements IPathEnum {
    /*-------------------------公共部分，请勿删除-------------------------*/
    LOGIN("login","登录"),
    IMAGE_CODE("image_code","图片验证码"),

    /*-------------------------自定义部分-------------------------*/
    QUERY_USER("query_user", "查询用户"),
    DEPOSIT_TOKEN("deposit_token","充值功能：获取充值Token"),
    PAY("pay", "充值功能：充值"),

    ;
    private final String pathCode;
    private final String message;

    private PathEnum(String pathCode, String message) {
        this.pathCode = pathCode;
        this.message = message;
    }

    @Override
    public String getPathCode() {
        return this.pathCode;
    }
}
