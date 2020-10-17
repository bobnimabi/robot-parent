package com.robot.bbin.base.ao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by mrt on 11/16/2019 11:47 AM
 * 查询投注总金额(游戏)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalBetGameAO {
    private String SearchData = "MemberBets";
    // 起始日期
    private String dateStart;
    // 结束日期
    private String dateEnd;
    // 会员的userID
    private String userID;
    private String barId;
    // 游戏种类
    private String gameKind;
}
