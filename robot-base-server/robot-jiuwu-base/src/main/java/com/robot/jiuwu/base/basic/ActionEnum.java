package com.robot.jiuwu.base.basic;

import com.robot.center.execute.IPathEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 * 注意：登录是不需要的
 * 登录使用 CommonActionEnum
 */
public enum ActionEnum implements IPathEnum {
    TOTAL_RECHARGE_DETAIL("total_recharge_detail", "查询打码明细"),
    QUERY_USER("query_user", "查询用户"),
    PAY("pay", "充值"),
    WITHDRAW_APPLY_LIST("withdraw_apply_list", "出款申请列表"),
    QUERY_USER_DETAIL("query_user_detail", "查询用户明细"),
    UPDATE_WITHDRAW_STATUS("update_withdraw_status", "更新出款订单状态"),
    UPDATE_REMARK2("update_remark2", "更新二次备注"),
    DO_LOCK("do_lock", "加入工单"),
    WITHDRAW_SUCCESS("withdraw_success", "出款成功"),
    ;
    private final String pathCode;
    private final String message;

    private ActionEnum(String pathCode, String message) {
        this.pathCode = pathCode;
        this.message = message;
    }

    @Override
    public String getpathCode() {
        return this.pathCode;
    }
}
