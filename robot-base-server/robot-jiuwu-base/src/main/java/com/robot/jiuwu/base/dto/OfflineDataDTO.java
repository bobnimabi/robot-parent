package com.robot.jiuwu.base.dto;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2020-05-11 10:36:50
 * 查询线下打款的DTO
 */
@Data
public class OfflineDataDTO {
    private int current;
    private int size;
    // 充值ID
    private String recordid;
    // 操作员
    private String username;
    // 选择类型
    // 0人工充值 1线上补单 2活动 3补单 4QQ扫码 5代理充值 6其他 9彩金扣除
    // 10误充扣除 20VIP特权 21绑定手机号 22邮件金币 23 红包雨
    private int[] types;
    // 游戏ID
    private String gameid;
    // VIP等级
    private String memberOrder;
    // 起始时间
    private String orderdatebegin;
    // 终止时间
    private String orderdateend;
    // 金额范围：最小金额
    private BigDecimal minamount;
    // 金额范围：最大金额
    private BigDecimal maxamount;
    // 备注测试
    private String remark;
    // 渠道ID
    private String source;
}