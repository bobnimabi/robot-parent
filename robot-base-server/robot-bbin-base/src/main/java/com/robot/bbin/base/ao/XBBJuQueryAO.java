package com.robot.bbin.base.ao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author mrt
 * @Date 2020/6/2 15:54
 * @Version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XBBJuQueryAO {
    // 平台编码
    private String gamekind;
    // 用户名,这个userId是什么意思?  局查询细节时候的参数
    // 从哪里来?现在不知道?   你意思是jack那边传递给你?    是从
    private String userid;
    //    主单号
    private String wagersid;
    // 固定参数
    private String SearchData="BetQuery";

    //游戏编码
    private String gameType;

}
