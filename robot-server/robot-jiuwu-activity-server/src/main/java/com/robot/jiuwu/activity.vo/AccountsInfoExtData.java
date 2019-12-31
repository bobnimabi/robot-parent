package com.robot.jiuwu.activity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by mrt on 2019/12/31 0031 13:16
 */
@Data
public class AccountsInfoExtData {
    @JSONField(name="TotalGrade")
    private BigDecimal TotalGrade;
    private Integer firstRecharge;
    private Integer countWithdraw;
    private Integer countRecharge;
    private String firstRechargeTime;
    private BigDecimal totalWithdraw;
    @JSONField(name="RegisterDate")
    private String RegisterDate;
    private Long source;
    private BigDecimal totalRecharge;
    private String firstTx;
    private Integer tempCodingNum;
    private Long UserID;
    @JSONField(name="TotalRevenue")
    private BigDecimal TotalRevenue;
    private BigDecimal agentRebate;
    @JSONField(name="TotalScore")
    private BigDecimal TotalScore;
    @JSONField(name="GameID")
    private Long GameID;
}
