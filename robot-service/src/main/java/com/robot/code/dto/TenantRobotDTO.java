package com.robot.code.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 机器人表
 * </p>
 *
 * @author admin
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TenantRobot对象", description="机器人表")
public class TenantRobotDTO extends Page implements Serializable {

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
    private Integer function;

    /**
     * 平台账号
     */
    private String platformAccount;

    /**
     * 平台密码
     */
    private String platformPassword;

    /**
     * 机器人描述
     */
    private String robotDesc;

    /**
     * 是否在线，状态： 0掉线 1在线
     */
    private Boolean isOnline;

    /**
     * 运行信息
     */
    private String info;

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

    /**
     * 自定义
     */
    private String opt;
    private String imageCode;

}
