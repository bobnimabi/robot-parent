package com.robot.code.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 机器人出款规则表
 * </p>
 *
 * @author admin
 * @since 2020-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TenantWithdrawRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 平台id
     */
    private Long channelId;

    /**
     * 机器人所属平台id
     */
    private Long platformId;

    /**
     * 功能：1.活动  2.出款  3.入款
     */
    private Long functionCode;

    /**
     * 出款循环时间间隔，单位：秒
     */
    private Long intervalSecond;

    /**
     * 同会员出款限制时间，单位：分钟
     */
    private Integer limitMinuts;

    /**
     * 出款最低金额，单位：元
     */
    private BigDecimal minAmount;

    /**
     * 出款最高金额，单位：元
     */
    private BigDecimal maxAmount;

    /**
     * 有备注是否拦截，1是 0否
     */
    private Boolean isLimitRemark;

    /**
     * 首次出款是否拦截，1是 0否
     */
    private Boolean isLimitFirst;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModifiedTime;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreateTime;


}
