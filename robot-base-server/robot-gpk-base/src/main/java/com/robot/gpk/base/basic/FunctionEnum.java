package com.robot.gpk.base.basic;

import com.robot.center.function.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 * 注意：登录是不需要的，登录在keepAlive里面直接调用Function
 */
public enum FunctionEnum implements IFunctionEnum {
    Image_CODE_SERVER("imageCodeServer"),
    TOTAL_RECHARGE_DETAIL_SERVER("totalRechargeDetailServer"),
    TOTAL_RECHARGE_SERVER("totalRechargeServer"),
    QUERY_USER_SERVER("queryUserServer"),
    PAY_SERVER("payServer"),
    QUERY_USER_DETAIL_SERVER("queryUserDetailServer"),
    QUERY_VIP_AMOUNT_SERVER("queryVipAmountServer"),
    PAY_TEMPSERVER("payTempServer"),
    UPDATE_REMARK2_SERVER("updateRemark2Server"),
    ;
    private String serverName;

    private FunctionEnum(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getFunctionServer() {
        return this.serverName;
    }
}
