package com.robot.jiuwu.base.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by mrt on 2019/12/30 0030 20:34
 */
@Data
public class RechargeData {
    // 输赢
    private BigDecimal score;
    // 投注量
    private BigDecimal grade;
    // 税收
    private BigDecimal revenue;


    private String kindName;

    // 游戏种类ID
    private Integer kindID;




}
