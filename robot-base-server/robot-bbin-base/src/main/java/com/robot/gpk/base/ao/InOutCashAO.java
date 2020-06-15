package com.robot.gpk.base.ao;

import lombok.Data;

/**
 * Created by mrt on 2020/5/13 13:26
 */
@Data
public class InOutCashAO {
    // 日期：起始，格式：2020-05-11
    private String start;
    // 日期：结束，格式：2020-05-12
    private String end;
    // 方式：0入款 1出款 2出入款
    private String methed;
    // 金额
    private String amount_value="";
    // 金额格式：0以上 1以下
    private String amount_than="0";
    // 次数
    private String times = "";
    // 次数格式：0以上 1以下
    private String than = "0";
    // 币别：RMB人民币，注意，此处只有RMB
    private String Currency="RMB";
    // 未知，写死1
    private String sortCol = "1";
    // 排序 1递减 0递增，写死1
    private String sort = "1";
    // 会员名称
    private String name;
    // 账号查询  agent代理 member会员
    private String accountType = "member";
    // 分析方式：member依会员 agent依代理
    private String analystType = "member";
    // 页码，写死1
    private String page = "1";
}
