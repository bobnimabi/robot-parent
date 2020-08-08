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
public class LuckyNoBO  {

    // 时间
    private LocalDateTime orderTime;
    // 注单编号
    private String platFormOrderNo;
    // 游戏类别
    private String gameName;
    // 厅主
    private String hall;
    // 账号
    private String userName;
    // 结果
    private String result;
    // 总投注（订单），单位元
    private BigDecimal rebateAmount;
    // 总派彩（订单），单位元
    private BigDecimal sendAmount;
    // 场次（申博电子）
    private String round;

    /*// 隐藏参数
    private String pageId;
    private String key;
    private String userId;
    //第三方平台注单号
    private String thirdPlatNo;*/

}
