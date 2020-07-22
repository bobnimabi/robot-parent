package com.robot.og.base.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 充值和下注返回结果
 */
@Data
public class AmountBO extends BetDetailBO {

    //充值金额
    private String inome;

}
