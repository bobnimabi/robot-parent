package com.robot.jiuwu.base.basic;

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
    TOTAL_RECHARGE_DETAIL_SERVER("totalRechargeDetailServer","totalrechage"),
    TOTAL_RECHARGE_SERVER("totalRechargeServer"," "),
    QUERY_USER_SERVER("queryUserServer"," "),

    PAY_SERVER("payServer"," "),
    QUERY_USER_DETAIL_SERVER("queryUserDetailServer"," "),
    QUERY_VIP_AMOUNT_SERVER("queryVipAmountServer"," "),
    PAY_TEMPSERVER("payTempServer"," "),
    UPDATE_REMARK2_SERVER("updateRemark2Server"," "),
    OFFLINE_RECHARGE_SERVER("offlineRechargeServer"," "),
    ONLINE_RECHARGE_SERVER("onlineRechargeServer"," "),
    BET_AMOUNT_AND_RECHARGE_SERVER("betAmountAndRechargeServer"," "),
/*-------------------改版-------------------------------*/
    QUERY_USER("onlineRechargeServer"," 2查询用户"),
    PAY("pay","充值")
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
