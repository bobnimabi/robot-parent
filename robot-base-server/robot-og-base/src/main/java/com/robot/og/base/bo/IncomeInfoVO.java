package com.robot.og.base.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 存款详情信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeInfoVO {

    // map列表
    private List<Map<String, String>> incomeList;

    // 存款总和
    private BigDecimal totalIncomeMoney = BigDecimal.ZERO;

}
