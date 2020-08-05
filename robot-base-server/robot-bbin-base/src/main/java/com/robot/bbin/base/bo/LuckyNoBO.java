package com.robot.bbin.base.bo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by mrt on 11/15/2019 3:04 PM
 * 局查询VO：对应Promotion的OrderNoQueryVO
 */
@Getter
@Setter
@Data
public class LuckyNoBO extends JuQueryBO {


    //第三方平台注单号
    private String thirdPlatNo;

}
