package com.robot.gpk.base.basic;

import com.robot.core.function.base.IPathEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 */
public enum PathEnum implements IPathEnum {

    /*-------------------------公共部分，请勿删除-------------------------*/
    FLUSH_SESSION("flush_session","刷新Session"),
    LOGIN("login","登录"),
    IMAGE_CODE("image_code","图片验证码"),
    SMS("sms","短信验证码"),

    /*-------------------------自定义部分-------------------------*/
    QUERY_USER("query_user", "查询用户"),
    PAY("pay", "充值"),
    DEPOSIT_TOKEN("DepositToken","打款前token，防表单提交"),
    VALIDATE_SMS("ValidateSms","登录短信校验"),

    ;
    private final String pathCode;
    private final String message;

    private PathEnum(String actionCode, String message) {
        this.pathCode = actionCode;
        this.message = message;
    }

    @Override
    public String getPathCode() {
        return this.pathCode;
    }
}
