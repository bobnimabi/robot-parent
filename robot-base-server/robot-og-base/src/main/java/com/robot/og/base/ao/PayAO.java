package com.robot.og.base.ao;

import com.robot.center.mq.PayCommonParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author mrt
 * @Date 2020/5/15 14:02
 * @Version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayAO extends PayCommonParams {

    private String type;
//    用户id
    private String memberId;
    //打款金额
    private String depositMoney;
    //备注
    private String depositMoneyRemark;
    private String depositPreStatus;
    private Double depositPre;
    private String otherPreStatus;
    private String otherPre;
    private String compBetCheckStatus;
    private String compBet;
    private String normalStatus;
//    打款类型
    private String depositPro;
    //随机token
    private String token;

}
