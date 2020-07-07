package com.robot.jiuwu.base.ao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by tanke on 2019/12/30 0030 20:51
 * 查询打码明细路径参数对象
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TotalRechargeAO {
    private String gameid;
    private String start;
    private String end;
    private String total;
    private String pageSize;
    private String currentPage;




}
