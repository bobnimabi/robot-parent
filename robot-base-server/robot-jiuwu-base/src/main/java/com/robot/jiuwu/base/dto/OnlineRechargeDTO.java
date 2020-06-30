package com.robot.jiuwu.base.dto;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;

@Data
public class OnlineRechargeDTO {
    private int current;
    private int size;
    // 订单号
    private String orderid="";
    // 游戏ID
    private String gameid="";
    // 金额范围：起始
    private BigDecimal amount_start;
    // 金额范围：终止
    private BigDecimal amount_end;
    // 订单状态：空：全部   0:未支付  1:已支付
    private String orderstatus="";
    // 支付时间：起始
    private String paydate_start="";
    // 支付时间：结束
    private String paydate_end="";
    // 创建时间：起始
    private String orderdate_start="";
    // 创建时间：结束
    private String orderdate_end="";
    // 商户
    private String sh="";
    // 渠道ID
    private String source="";
    // 入口系统：空全部  0线上入口  1快捷入口  100银商入口
    private String ordertype="";
    // 支付类型：空全部  100微信支付  200支付宝  300快捷支付 301银商支付  400银行卡
    private String shareid="";
    // 流水ID
    private String onlineid="";
    // 备注
    private String payaddress="";
    // VIP等级
    private String memberOrder="";

}