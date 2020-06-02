package com.robot.bbin.base.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by mrt on 2020/5/13 14:09
 */
@Data
public class InOutCashData {
    private String index;
    // 代理账号
    private String agent;
    // 会员账号
    private String member;
    // 入款次数
    private String dep_total;
    // 入款金额
    private BigDecimal dep_amount;
    // 出款次数
    private String wit_total;
    // 出款金额
    private BigDecimal wit_amount;
}
