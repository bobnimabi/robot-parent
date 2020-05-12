package com.robot.jiuwu.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrt on 2019/12/30 0030 20:51
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TotalRechargeDTO {
    private String userName;
    private String beginDate;
    private String endDate;
}
