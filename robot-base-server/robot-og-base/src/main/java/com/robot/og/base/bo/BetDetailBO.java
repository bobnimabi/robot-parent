package com.robot.og.base.bo;

import com.bbin.common.client.TenantBetVo;
import com.bbin.common.client.TenantChannelUserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BetDetailBO implements Serializable {
    //用户信息
    private String userName;
    //当前游戏投注
    private String tenantBets;
    //总下注
    private String totalBet;
    //总损益
    private String totalLoss;

}
