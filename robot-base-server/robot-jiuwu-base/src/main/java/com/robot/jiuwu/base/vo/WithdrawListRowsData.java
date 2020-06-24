package com.robot.jiuwu.base.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by mrt on 2020/1/1 0001 17:29
 */
@Data
public class WithdrawListRowsData {
        // 订单id
        private Long id;
        // userName
        private Long gameid;
        // 代付商家id？这个猜的
        private Integer pendingId;
        // VIP
        private Integer memberOrder;
        // 备注
        private String remark;
        // 渠道ID
        private String source;
        // 操作时间？这个猜的
        private String opttime;
        private Long userid;
        // 总充值
        private BigDecimal totalRecharge;
        // 手续费
        private BigDecimal fee;
        // 审核人
        private String remarkUser;
        private Long firstId;
        // 银行名称
        private String bank;
        // 昵称
        private String nickname;
        // 是否标记
        private Integer lock;
        // 总提现
        private BigDecimal totalWithdraw;
        //
        private Integer pendingStatus;
        //申请时间
        private String applytime;
        private Integer firstTx;
        // 本次提现金额，单位：分,获取后立刻转换成元
        private BigDecimal money;

        private Long optuserid;
        // 真实姓名
        private String name;
        // 更新时间
        private String updatetime;
        // 提现类型：1银行卡 2支付宝
        private Integer paytype;
        // 银行账号
        private String account;
        // 订单状态：0审核中 1审核通过 2预出款 3出款中 4已成功
        // -1已退回 -2已拒绝 5代付 6代付中
        private Integer status;
        // 操作人
        private String username;
        // 二次备注
        private String remark2;
}
