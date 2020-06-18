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
 * 登录输入框列表
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TenantRobotTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属平台的id  tenant_platform表的id
     */
    private Long platformId;

    /**
     * 栏位排序
     */
    private Integer filedOrder;

    /**
     * 1. 输入框   2. 图片验证码
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
     * 链接地址
     */
    private String filedUrl;

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
