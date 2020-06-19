package com.robot.bbin.base.ao;

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
    // 平台编码
    private String pageId;
    private String key;
    // 日期
    private String rounddate;
    //游戏编码
    private String gameType;
}
