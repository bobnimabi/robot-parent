package com.robot.bbin.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/12/2019 8:23 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalBetGameVO {
    // 游戏类别
    private String gameName;
    // 单量
    private Integer num;
    //总投注
    private BigDecimal totalBetByGame;
    // 总派彩
    private BigDecimal totalPayByGame;
}
