package com.robot.og.base.basic;

import com.robot.core.function.base.IPathEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 * 数据库中id  1~20内
 */
public enum PathEnum implements IPathEnum {
    /*-------------------------公共部分，请勿删除-------------------------*/
    LOGIN("login","登录"),
    IMAGE_CODE("image_code","图片验证码"),

    /*-------------------------自定义部分-------------------------*/
    QUERY_USER("query_user", "查询用户"),
    DEPOSIT_TOKEN("deposit_token","充值功能：获取充值Token"),
    PAY("pay", "打款"),
    GET_DETAIL("get_bet_detail", "查询下注详情"),
    GETTOTALAMOUNT("get_total_amount", "查询总打码量"),
    GETRECHARGE("get_recharge", "查询下注"),
    QUERY_RECORD("query_record", "充值功能：充值"),
    QUERY_BALANCE("query_balance", "查询余额"),
    QUERY_LEVEL("query_level", "查询层次"),
    QUERY_ODERNO("query_oderno", "查询注单号"),
    QUERY_ACCOUNT("query_acount", "查询账户"),
    QUERY_USER_INFO("query_user_info", "查询用户信息"),
    QUERY_USERRECORD("query_user_record", "查询用户记录"),

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
