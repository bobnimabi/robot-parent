package com.robot.bbin.base.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by mrt on 2020/5/12 19:08
 */
@Data
public class TokenBO {
    // 平台编码
    private String gamekind;

    private String userid;
    //    主单号
    private String wagersid;
    // 固定参数
    private String SearchData="BetQuery";

    //游戏编码
    private String gameType;
    private String token;
}
