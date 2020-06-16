package com.robot.bbin.base.ao;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import lombok.Data;

/**
 * @Author mrt
 * @Date 2020/6/1 13:36
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
     * bb电子是2，先临时写死
     */
    private String barId = "2";


}
