package com.robot.liantong.base.basic;

import com.robot.center.execute.IActionEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 * 注意：
 *  1.登录不需要
 *  2.图片验证码不需要
 *  3.手机短信不需要
 *  4.刷新session不需要
 *  使用 CommonActionEnum
 */
public enum ActionEnum implements IActionEnum {
    CHECK_NEED_VERIFY("check_need_verify", "登录前检查"),
    IS_LOGIN("is_login", "验证是否登录"),
    BUY_CARD_CHECK("buy_card_check", "买卡提交前：参数校验"),
    BUY_CARD_SUBMIT("buy_card_submit", "买卡提交"),
    CHECK_AGREEMENT("check_agreement", "校验用户是否存在"),
    SEND_PHONE_VERIFY_CODE("send_phone_verify_code", "买卡前:发送手机短信"),
    CHECK_PHONE_VERIFY_CODE("check_phone_verify_code", "买卡前:校验手机短信"),
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

