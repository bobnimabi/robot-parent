package com.robot.jiuwu.activity.basic;

import com.robot.center.execute.IActionEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 */
public enum ActionEnum implements IActionEnum {
    QUERY_USER("query_user","查询用户"),
    PAY("pay","充值"),
    IMAGE_CODE("image_code","图片验证码"),
    ;
    private final String actionCode;
    private final String message;

    private ActionEnum(String actionCode,String message) {
        this.actionCode = actionCode;
        this.message = message;
    }

    @Override
    public String getActionCode() {
        return this.actionCode;
    }
}
