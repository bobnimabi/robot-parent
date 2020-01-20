package com.robot.jiuwu.base.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Created by mrt on 2020/1/1 0001 15:48
 * 提现申请列表：条件查询DTO
 */
@Data
public class WithdrawListDTO {
    // 订单号
    private String id;
    // 银行卡号
    private String account;
    // 申请时间：开始
    private String applytime_start;
    // 申请时间：结束
    private String applytime_end;
    // 游戏ID：相当于其他平台的userName
    private String gameid;
    // 是否充值 0未充值 1已充值
    private String isRecharge;
    // 是否二次备注"true","false"
    private String isremark2="false";
    // VIP等级
    private String memberOrder;
    // 金额：起始
    private BigDecimal money_start;
    // 金额：结束
    private BigDecimal money_end;
    // 提现类型：1银行卡 2支付宝
    private Integer paytype;
    // 渠道ID
    private String source;
    // 订单状态：0审核中 1审核通过 2预出款 3出款中 4已成功
    // -1已退回 -2已拒绝 5代付 6代付中
    // 多个状态以逗号分隔
    private String status;
    // 更新时间：开始
    private String updatetime_start;
    // 更新时间：结束
    private String updatetime_end;
    // 操作人
    private String username;
    // 是否标记 0未标记 1已标记
    private String lock;
    // 代付商家
    private String pendingName;
    // 用户姓名
    private String name;
    // 当前页码
    private Integer current;
    // 当前页大小
    private Integer size;
}
