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
 * 机器人表
 * </p>
 *
 * @author admin
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TenantRobotRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机器人表
     */
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
     * 连接池：最大连接数
     */
    private Integer maxTotal;

    /**
     * 连接池：每个路由并发数
     */
    private Integer defaultMaxPerRoute;

    /**
     * 从连接池获取连接超时，单位：秒
     */
    private Integer connectRequestTimeout;

    /**
     * 连接超时，单位：秒
     */
    private Integer connectTimeout;

    /**
     * 请求超时，单位：秒
     */
    private Integer socketTimeout;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 超时时间，单位：秒
     */
    private Integer keepAliveTimeout;

    /**
     * 是否有效 1有效  0无效
     */
    private Boolean isValid;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModifiedTime;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreateTime;


}
