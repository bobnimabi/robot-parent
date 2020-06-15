package com.robot.gpk.base.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by mrt on 2019/4/13 0013 下午 12:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayResponseBO implements Serializable {
    //机器人订单号
    private String robotRecordNo;
    //外部订单号
    private String outPayNo;
    // 打款金额
    private BigDecimal paidAmount;
}
