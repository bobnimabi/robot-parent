package com.robot.jiuwu.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by mrt on 2020/5/11 11:01
 * 线下充值总金额
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OfflineRechargeData {
    private BigDecimal amount;

}
