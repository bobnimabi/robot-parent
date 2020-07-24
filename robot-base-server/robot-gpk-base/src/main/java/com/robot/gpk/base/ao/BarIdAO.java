package com.robot.gpk.base.ao;

import lombok.Data;

/**
 * 局查询前：获取BarId
 * @Author mrt
 * @Date 2020/6/19 16:44
 * @Version 2.0
 */
@Data
public class BarIdAO {
    private String SearchData;
    private String GameKind;
    private String date_start;
    private String date_end;
}
