package com.robot.bbin.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by mrt on 2019/4/12 0012 下午 9:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayMoneyDTO implements Serializable {
    //任务id
    private long taskId;
    //打款账号
    private String username;
    //打款金额，单位：元
    private BigDecimal paidAmount;
    //租户
    private Long tenantId;
    //渠道
    private Long channelId;
    //打款备注
    private String memo;
    //memberId
    private String memberId;
    //外部订单号
    private String outPayNo;
    //主题
    private String theme;
    //是否稽核
    private Boolean isAudit = false;

    /**
     * 打码倍数
     */
    private Integer multipleTransaction;
    /**
     * 前台备注
     */
    private String frontMemo;
}
