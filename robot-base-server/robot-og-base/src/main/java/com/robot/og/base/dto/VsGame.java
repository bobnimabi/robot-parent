package com.robot.og.base.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@ApiModel(value="VsGame对象", description="某个包网平台的游戏分类")
public class VsGame implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long tenantId;

    private Long gameId;

    private String gameCode;

    private String gameName;

 //   @ApiModelProperty(value = "渠道id")
    private Long channelId;

   // @ApiModelProperty(value = "0关闭 1开启")
    private Integer status;

    private LocalDateTime gmtCreateTime;

    private LocalDateTime gmtModifiedTime;

    private BigDecimal payRatio;


}