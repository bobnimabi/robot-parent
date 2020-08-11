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
    //余额
    private BigDecimal balance;
    //详细
    private List<TenanteBetBO> tenantBets = new ArrayList<>();
    //总损益
    private BigDecimal totalLoss;

    //用户信息
    private TenantChannelUserVo tenantChannelUser;
    //总实际投注
   private BigDecimal totalBet;
    //存款详细
    private IncomeInfoVO incomeInfoVO;

}


