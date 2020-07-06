package com.robot.bbin.base.ao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author mrt
 * @Date 2020/6/2 15:54
 * @Version 2.0   获取token 路径参数对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenAO {
    // 平台编码
    private String gamekind;

    private String userid;
    //    主单号
    private String wagersid;
    // 固定参数
    private String SearchData="BetQuery";

    //游戏编码
    private String gameType;

}
