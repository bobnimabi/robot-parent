package com.robot.gpk.base.ao;

import lombok.Data;

/**
 * @Author mrt
 * @Date 2020/6/2 12:19
 * @Version 2.0
 */
@Data
public class PayAO {
    // 搜索名称：会员账号
    private String search_name;
    // 来自于查询余额的返回
    private String user_id;
    // 来自于查询余额的返回
    private String hallid;
    // 来自于查询余额的返回
    private String CHK_ID;
    // 会员账号
    private String user_name;
    // 来自于查询余额的返回
    private String date;
    // 币种
    private String Currency = "RMB";
    private String abamount_limit = "0";
    //存款金额
    private String amount;
    //备注
    private String amount_memo;
    private String CommissionCheck = "Y";
    //存入项目 ARD8表示活动优惠，其他的见网页
    private String DepositItem = "ARD8";
    //综合打码量稽核  1稽核，不传不稽核
    private String ComplexAuditCheck;
    // 综合打码量，不稽核传0
    private String complex;
}
