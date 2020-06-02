package com.robot.bbin.base.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/15/2019 3:10 PM
 * 消消除
 */
@Data
public class BreakerQueryBO extends JuQueryBO {
    // 总投注（限定时间内，一般是当天）
    private BigDecimal totalBetAmount;
    // 消消除：闯关局数
    private Integer accumulativeWins;


}
