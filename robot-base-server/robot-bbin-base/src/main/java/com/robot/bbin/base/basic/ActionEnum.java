package com.robot.bbin.base.basic;

import com.robot.center.execute.IActionEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 */
public enum ActionEnum implements IActionEnum {
    QUERY_BALANCE("query_balance","查询会员余额"),
    JU_QUERY("ju_query","局查询"),
    JU_QUERY_DETAIL("ju_query_detail","局查询:窗口信息"),
    PAY_ORDER("pay_order","出款"),
    QUERY_LEVEL("query_level","查询层级"),
    PAY("pay","充值"),
    TOTAL_BET_BY_GAME("total_bet_by_game","查询消消除单个游戏投注总金额"),
    BET_ANALYSIS("bet_analysis","下注分析"),
    BET_ANALYSIS_DETAIL("bet_analysis_detail","下注分析详细"),
    IN_OUT_CASH("in_out_cash","出入款统计")

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
