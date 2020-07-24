package com.robot.gpk.base.bo;

import lombok.Data;

/**
 * Created by mrt on 11/15/2019 3:04 PM
 * 局查询VO：对应Promotion的OrderNoQueryVO
 */
@Data
public class JuQueryDetailBO extends JuQueryBO{
    // 消除次数
    private Integer level;

    // 消消除:游戏编码,这个带上最好
    private String gameCode;

    // 彩球个数
    private Integer ballNumber;
}
