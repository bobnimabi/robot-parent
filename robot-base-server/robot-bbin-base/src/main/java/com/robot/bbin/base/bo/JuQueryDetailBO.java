package com.robot.bbin.base.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by mrt on 11/15/2019 3:04 PM
 * 局查询VO：对应Promotion的OrderNoQueryVO
 */
@Data
public class JuQueryDetailBO extends JuQueryBO{
    private Integer level;

    // 隐藏参数
    private String pageId;
    private String key;
}
