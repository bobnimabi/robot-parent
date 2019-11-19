package com.robot.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TenantRobotAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 包网平台的id （机器人用于）tenant_channel表的id
     */
    private Long channelId;

    /**
     * 所属平台的id  tenant_platform表的id
     */
    private Long platformId;

    /**
     * 功能：1.活动  2.出款  3.入款
     */
    private Integer functionCode;

    /**
     * 动作编码
     */
    private String actionCode;

    /**
     * 动作解释
     */
    private String actionMemo;

    /**
     * 访问url
     */
    private String actionUrl;

    /**
     * 是否有效 1有效  0无效
     */
    private Boolean isValid;

    /**
     * 同一机器人请求限制，单位：秒，0表示无限制
     */
    private Integer robotTimeLimit;

    /**
     * 同一IP请求限制，单位：秒，0表示无限制
     */
    private Integer ipTimeLimit;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModifiedTime;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreateTime;


}
