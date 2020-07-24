package com.robot.gpk.base.basic;

import com.robot.core.function.base.IPathEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 */
public enum PathEnum implements IPathEnum {
    /*-------------------------公共部分，请勿删除-------------------------*/
    LOGIN("login","登录"),
    IMAGE_CODE("image_code","图片验证码"),
    PAY("pay", "充值功能：充值"),
    /*-------------------------自定义部分-------------------------*/
    QUERY_USER("query_user", "查询用户"),
    DEPOSIT_TOKEN("deposit_token","充值功能：获取充值Token"),
    JU_QUERY_DETAIL("ju_query_detail","局查询:窗口信息"),
    JU_QUERY("ju_query","局查询"),
    TOTAL_BET_BY_GAME("total_bet_by_game","查询消消除单个游戏投注总金额"),
    BAR_ID("bar_id","局查询"),
    GET_USERID("bar_id","局查询"),
    ;


    private final String pathCode;
    private final String message;

    private PathEnum(String pathCode, String message) {
        this.pathCode = pathCode;
        this.message = message;
    }

    @Override
    public String getPathCode() {
        return this.pathCode;
    }
}
