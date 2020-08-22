package com.robot.og.base.bo;

import com.bbin.common.client.TenantChannelUserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 充值和下注返回结果
 */
@Data
public class AmountBO  {

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

    //充值金额
    private BigDecimal income;

}
