package com.robot.jiuwu.login.basic;

import com.robot.center.execute.IActionEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 * 注意：登录是不需要的
 * 登录使用 CommonActionEnum
 */
public enum ActionEnum implements IActionEnum {
    QUERY_TOTAL_RECHARGE_SERVER("QUERY_TOTAL_RECHARGE_SERVER","查询用户"),
    QUERY_USER("query_user","查询用户"),
    PAY("pay","充值"),
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
