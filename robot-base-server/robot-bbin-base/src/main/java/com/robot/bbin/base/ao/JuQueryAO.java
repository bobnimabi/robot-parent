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
    // 注单编号
    private String orderNo;
    // 平台编码
    private String gameKind;

    /**
     * 局查询额外参数
     * bb电子是2，先临时写死,因为局查询目前只有新濠在用
     */
    private String barId = "2";


}
