package com.robot.og.base.bo;

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
public class TenantBetDetailBO implements Serializable {
    //用户信息
    private TenantChannelUserVo tenantChannelUser;
    //总实际投注
    private BigDecimal totalBet;
    //总损益
    private BigDecimal totalLoss;
    //余额
    private BigDecimal balance;
    //详细
    private List<TenanteBetBO> tenantBets = new ArrayList<>();
    // 充值
    private BigDecimal income;
}
