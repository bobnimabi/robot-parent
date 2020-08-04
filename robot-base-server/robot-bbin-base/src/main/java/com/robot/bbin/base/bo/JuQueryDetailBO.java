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
    // 消除次数
    private Integer accumulativeWins;   //accumulativeWins

    // 消消除:游戏编码,这个带上最好
    private String gameCode;

    // 彩球个数
    private Integer ballNumber;
}
