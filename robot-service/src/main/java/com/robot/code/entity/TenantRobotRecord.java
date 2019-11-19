package com.robot.code.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 机器人打款记录流水
 * </p>
 *
 * @author admin
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TenantRobotRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 平台id，机器人所属平台
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
     * 机器人id,tenant_robot的id
     */
    private Long robotId;

    /**
     * 动作,t enant_robot_action表的action_code
     */
    private String actionCode;

    /**
     * 外部打款订单号
     */
    private String externalOrderNo;

    /**
     * 1发送中 2响应成功 -1 响应失败
     */
    private Integer status;

    /**
     * 请求信息
     */
    private String reqInfo;

    /**
     * 响应信息
     */
    private String respInfo;

    /**
     * 是否有效 1有效  0无效
     */
    private Boolean isValid;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreateTime;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModifiedTime;


}
