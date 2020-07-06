package com.robot.jiuwu.base.basic;

import com.robot.core.function.base.IPathEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 */
public enum PathEnum implements IPathEnum {



    TOTAL_RECHARGE_DETAIL("total_recharge_detail", "查询打码明细"),
    QUERY_USER("query_user", "查询用户"),
    LOGIN("login", "登录"),
    PAY("pay", "充值"),
    IMAGE_CODE("image_code", "获取图片验证码"),
    WITHDRAW_APPLY_LIST("withdraw_apply_list", "出款申请列表"),
    QUERY_USER_DETAIL("query_user_detail", "查询用户明细"),
    UPDATE_WITHDRAW_STATUS("update_withdraw_status", "更新出款订单状态"),
    UPDATE_REMARK2("update_remark2", "更新二次备注"),
    DO_LOCK("do_lock", "加入工单"),
    WITHDRAW_SUCCESS("withdraw_success", "出款成功"),
    OFFLINE_RECHARGE("offline_recharge", "线下充值总金额"),
    ONLINE_RECHARGE("online_recharge", "线上充值总金额"),

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
