package com.robot.bbin.base.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by mrt on 2020/5/13 11:12
 */
@Data
public class BetData {
    private String index;
    private String agentId;
    // 给BetDetailDTO的参数：listid
    private String key;
    // 注单数
    private String wagersTotal;
    // 下注金额
    private BigDecimal wagersAmount;
    // 有效投注
    private BigDecimal commissionable;
    // 派彩
    private BigDecimal payoff;
    // 胜率
    private String percentage;
    // 会员账号
    private String member;
    // 代理账号
    private String agent;
}
