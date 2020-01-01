package com.robot.jiuwu.base.basic;

import com.robot.center.execute.IActionEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 * 注意：登录是不需要的
 * 登录使用 CommonActionEnum
 */
public enum ActionEnum implements IActionEnum {
    TOTAL_RECHARGE_DETAIL("total_recharge_detail","查询打码明细"),
    QUERY_USER("query_user","查询用户"),
    PAY("pay","充值"),
    QUERY_USER_DETAIL("query_user_detail","查询用户明细"),
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
