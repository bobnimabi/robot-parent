package com.robot.bbin.base.dto;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import lombok.Data;

/**
 * @Author mrt
 * @Date 2020/6/1 13:36
 * @Version 2.0
 */
@Data
public class JuQueryDetailDTO extends OrderNoQueryDTO {

    /**
     * 局查询额外参数
     */
    private String barId;

    /**
     * 消消除狂口信息需要的参数
     */
    private String pageId;
    private String key;
    private String gameType;//游戏编码
}
