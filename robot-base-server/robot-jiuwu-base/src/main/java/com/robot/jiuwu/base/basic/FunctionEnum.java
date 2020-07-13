package com.robot.jiuwu.base.basic;


import com.robot.core.function.base.IFunctionEnum;

/**
 * Created by mrt on 11/15/2019 8:27 PM
 */
public enum FunctionEnum implements IFunctionEnum {
	LOGIN_SERVER("loginServer"),
    QUERY_USER_SERVER("queryUserServer"),
    PAY_SERVER("payServer"),
	QUERY_VIP_AMOUNT_SERVER("queryVipAmountServer"),
	Image_CODE_SERVER("imageCodeServer"),
	TOTAL_RECHARGE_DETAIL_SERVER("totalRechargeDetailServer"),
	TOTAL_RECHARGE_SERVER("totalRechargeServer"),
	QUERY_USER_DETAIL_SERVER("queryUserDetailServer"),
	PAY_TEMPSERVER("payTempServer"),
	UPDATE_REMARK2_SERVER("updateRemark2Server"),
	OFFLINE_RECHARGE_SERVER("offlineRechargeServer"),
	ONLINE_RECHARGE_SERVER("onlineRechargeServer"),
	BET_AMOUNT_AND_RECHARGE_SERVER("betAmountAndRechargeServer")
	;

	private final String serverName;

    private FunctionEnum(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getName() {
        return this.serverName;
    }
}
