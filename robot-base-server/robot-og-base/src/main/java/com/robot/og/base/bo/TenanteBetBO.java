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
public class TenanteBetBO implements Serializable {

    private Long gameId;

    private BigDecimal lossAmount;
    //实际投注
    private BigDecimal betAmount;

    private Long tenantId;

}


