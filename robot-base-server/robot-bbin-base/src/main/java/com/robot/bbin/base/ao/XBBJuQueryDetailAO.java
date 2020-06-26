package com.robot.bbin.base.ao;

import lombok.Data;

/**
 * @Author mrt
 * @Date 2020/6/2 15:54
 * @Version 2.0
 */
@Data
public class XBBJuQueryDetailAO {
    // 平台编码
    private String gamekind;
    // 用户名
    private String userid;
    //    主单号
    private String wagersid;
    // 固定参数
    private String SearchData;

    //游戏编码
    private String gameType;

}
