package com.robot.jiuwu.pay.common;

/**
 * Created by mrt on 2020/1/11 0011 15:27
 */
public class JiuWuPayConsts {

    /**
     * 出款订单状态
     * 0审核中 1审核通过 2预出款 3出款中 4已成功
     * -1已退回 -2已拒绝 5代付 6代付中
     */
    // 出款：审核中
    public static final int AUDIT_ING = 0;
    // 出款：审核通过
    public static final int AUDIT_SUCCESS = 1;
    // 出款：预出款
    public static final int WITHDRAW_PRE = 2;
    // 出款：出款中
    public static final int WITHDRAW_ING = 3;
    // 出款：出款成功
    public static final int WITHDRAW_SUCCESS = 4;
    // 出款：已退回
    public static final int WITHDRAW_BACK = -1;
    // 出款：已拒绝
    public static final int WITHDRAW_REFUSE = -2;
    // 出款：代付
    public static final int WITHDRAW_AGENT = 5;
    // 出款：代付中
    public static final int WITHDRAW_AGENT_ING = 6;


}
