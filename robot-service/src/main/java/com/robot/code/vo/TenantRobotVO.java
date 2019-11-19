package com.robot.code.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class TenantRobotVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "机器人表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "包网平台的id （机器人用于）tenant_channel表的id")
    private Long channelId;

    @ApiModelProperty(value = "所属平台的id  tenant_platform表的id")
    private Long platformId;

    @ApiModelProperty(value = "功能：1.活动  2.出款  3.入款")
    private Integer function;

    @ApiModelProperty(value = "平台账号")
    private String platformAccount;

    @ApiModelProperty(value = "平台密码")
    private String platformPassword;

    @ApiModelProperty(value = "机器人描述")
    private String robotDesc;

    @ApiModelProperty(value = "是否在线，状态： 0掉线 1在线")
    private Boolean isOnline;

    @ApiModelProperty(value = "状态： 0停止  1运行")
    private Boolean status;

    @ApiModelProperty(value = "运行信息")
    private String info;

    @ApiModelProperty(value = "是否有效 1有效  0无效")
    private Boolean isValid;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModifiedTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreateTime;


}
