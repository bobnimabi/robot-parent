package com.robot.code.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 某个包网平台的游戏分类
 * </p>
 *
 * @author admin
 * @since 2019-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//(value="VsGame对象", description="某个包网平台的游戏分类")
public class VsGame implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long tenantId;

    private Long gameId;

    private String gameCode;

    private String gameName;

   // "渠道id")
    private Long channelId;

   // value = "0关闭 1开启")
    private Integer status;

    private LocalDateTime gmtCreateTime;

    private LocalDateTime gmtModifiedTime;

    private BigDecimal payRatio;


}
