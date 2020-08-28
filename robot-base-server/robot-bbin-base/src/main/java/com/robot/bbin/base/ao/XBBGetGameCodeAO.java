package com.robot.bbin.base.ao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrt on 11/16/2019 11:47 AM
 * 查询投注总金额(游戏)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XBBGetGameCodeAO {

    //游戏注单号
    private String orderNo;

    private String gameKind;

    // 会员的userID
    private String userID;


}
