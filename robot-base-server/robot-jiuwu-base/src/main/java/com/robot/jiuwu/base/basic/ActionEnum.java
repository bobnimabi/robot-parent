package com.robot.jiuwu.base.basic;

import com.robot.center.execute.IActionEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 * 注意：登录是不需要的
 * 登录使用 CommonActionEnum
 */
public enum ActionEnum implements IActionEnum {
    TOTAL_RECHARGE_DETAIL("total_recharge_detail", "查询打码明细"),
    QUERY_USER("query_user", "查询用户"),
    PAY("pay", "充值"),
    WITHDRAW_APPLY_LIST("withdraw_apply_list", "出款申请列表"),
    QUERY_USER_DETAIL("query_user_detail", "查询用户明细"),
    UPDATE_WITHDRAW_STATUS("update_withdraw_status", "更新出款订单状态"),
    UPDATE_REMARK2("update_remark2", "更新二次备注"),
    DO_LOCK("do_lock", "加入工单"),
    WITHDRAW_SUCCESS("withdraw_success", "出款成功"),
    OFFLINE_RECHARGE("offline_recharge", "线下充值总金额"),
    ONLINE_RECHARGE("online_recharge", "线上充值总金额"),
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
