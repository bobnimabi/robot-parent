package com.robot.suxiao.base.basic;

import com.robot.center.execute.IActionEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 * 注意：登录是不需要的
 * 登录使用 CommonActionEnum
 */
public enum ActionEnum implements IActionEnum {
    MAIN("main", "登录前获取相关参数"),
    CANCEL_CARD("cancel_card", "销卡"),
    QUERY_USER_INFO("query_user_info", "提现前：查询用户信息"),
    WITHDRAW("withdraw", "提现前：查询银行卡id"),

    ;
    private final String actionCode;
    private final String message;

    private ActionEnum(String actionCode, String message) {
        this.actionCode = actionCode;
        this.message = message;
    }

    @Override
    public String getActionCode() {
        return this.actionCode;
    }
}

