package com.robot.bbin.base.ao;

import lombok.Data;

/**
 *
 * @Author mrt
 * @Date 2020/6/2 15:54  XBB局查询细节参数
 * @Version 2.0
 *
 */
@Data
public class XBBJuQueryDetailAO {
    // 注单编码
    private String orderNo;
    // 平台编码
    private String pageId;
    private String key;
    // 日期
    private String rounddate;
    //游戏编码
    private String gameType;

    // XBB电子:局查询细节专用
    // 局查询前置接口:获取Token使用该参数
    private String userId;
   //请求体加token
    private String token;
}
