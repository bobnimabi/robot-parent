package com.robot.jiuwu.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrt on 2020/1/8 0008 12:20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateRemark2DTO {
    // id
    private long id;
    // 二次备注字段
    private String remark2;
}
