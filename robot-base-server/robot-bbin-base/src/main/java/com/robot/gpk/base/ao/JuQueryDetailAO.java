package com.robot.gpk.base.ao;

import lombok.Data;

/**
 * @Author mrt
 * @Date 2020/6/2 15:54
 * @Version 2.0
 */
@Data
public class JuQueryDetailAO {
    // 注单编码
    private String orderNo;
    private String pageId;
    private String key;
    //游戏编码
    private String gameType;
}
