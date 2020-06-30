package com.robot.jiuwu.base.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by mrt on 2020/5/11 11:01
 * 线上充值总金额
 */
@Data
public class OnlineRechargeData {
    // 线上充值总金额
    private BigDecimal amount;
    // 充值次数
    private int num;

}
