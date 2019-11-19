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
 * 用户参与某个活动的申请填写数据模板明细
 * </p>
 *
 * @author admin
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TenantRobotTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动模板申请细节表，给每个活动添加栏位
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 表tenant的id，租户的id
     */
    private Long tenantId;

    /**
     * 表tenant_channel的id，渠道id
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
     * 栏位排序
     */
    private Integer filedOrder;

    /**
     * 
1. 输入框   2. 图片验证码
     */
    private Integer fieldType;

    /**
     * 展示名称
     */
    private String fieldDisplayName;

    /**
     * 提示
     */
    private String fieldTips;

    /**
     * 账号：platformAccount  密码：platformPassword 图片验证码：imageCode   OPT：opt
     */
    private String filedName;

    /**
     * 是否有效 1有效  0无效
     */
    private Boolean isValid;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreatedTime;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModifiedTime;


}
