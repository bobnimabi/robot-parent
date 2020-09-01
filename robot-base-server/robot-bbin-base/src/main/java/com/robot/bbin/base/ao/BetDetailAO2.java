package com.robot.bbin.base.ao;

import lombok.Data;

/**
 * Created by mrt on 2020/5/12 19:08
 */
@Data
public class BetDetailAO2 {
    // Bet接口的key
    private String listid;
    // 日期：开始
    private String start;
    // 日期：结束
    private String end;
    // 同BetDTO
    private String game;
    // 同BetDTO
    private String currency = "RMB";
    // 同BetDTO
    private String gametype = "ALL";
}
