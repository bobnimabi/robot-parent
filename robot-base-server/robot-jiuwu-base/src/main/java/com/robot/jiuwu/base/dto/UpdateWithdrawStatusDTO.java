package com.robot.jiuwu.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by mrt on 2020/1/1 0001 20:15
 * 批量更新状态
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateWithdrawStatusDTO {
    // 多个id以逗号分隔
    private String ids;

    // 订单状态：0审核中 1审核通过 2预出款 3出款中 4已成功
    // -1已退回 -2已拒绝 5代付 6代付中
    private Integer status;
}
