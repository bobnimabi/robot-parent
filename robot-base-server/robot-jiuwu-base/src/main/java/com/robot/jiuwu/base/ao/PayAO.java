package com.robot.jiuwu.base.ao;

import com.robot.center.mq.PayCommonParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author mrt
 * @Date 2020/5/15 14:02  需修改
 * @Version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayAO extends PayCommonParams {
    // 存入帐号
    private String AccountsString;
    // 应该是防止表单重复提交的
    private String DepositToken;
    // 类型：人工存提：4 优惠活动：5 返水：6 补发派彩：7 其他 99
    private String Type;
    // 实际存提，true或false
    private String IsReal;
    // 前台备注
    private String PortalMemo;
    // 后台备注
    private String Memo;
    // 登录密码
    private String Password;
    // 存款金额
    private String Amount;
    // 金额字符串
    private String AmountString;
    // 时间戳
    private String TimeStamp;
    // 稽核方式 免稽核：None 存款稽核：Deposit 优惠稽核：Discount
    private String AuditType;
    // 稽核金额
    private String Audit;
    // 打款前：调接口DepositTokenServer获取的防表单重复提交的Token
    private String depositToken;

    //新增
    private String gameId;
    //备注
    private String remark;

}
