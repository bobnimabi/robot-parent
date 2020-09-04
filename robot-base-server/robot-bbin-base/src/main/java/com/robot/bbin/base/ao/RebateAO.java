package com.robot.bbin.base.ao;

import lombok.Data;

@Data
public class RebateAO {
    // 日期：起始
    private String start;
    // 日期：结束
    private String end;

    // 会员名称
    private String name;
}
