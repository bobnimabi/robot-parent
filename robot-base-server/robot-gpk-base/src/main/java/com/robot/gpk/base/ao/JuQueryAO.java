package com.robot.gpk.base.ao;

import lombok.Data;

/**
 * @Author mrt
 * @Date 2020/6/1 13:36   BB局查询参数
 * @Version 2.0
 */
@Data
public class JuQueryAO {
    private String SearchData = "BetQuery";
    // 每页大小
    private String Limit="50";
    // 时间倒排
    private String Sort="DESC";
    // 注单编号
    private String orderNo;
    // 平台编码
    private String gameKind;

    /**
     * 局查询额外参数
     * bb电子:2
     */
    private String barId;


}
