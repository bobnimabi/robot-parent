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
 * 包网平台分类配置
 * </p>
 *
 * @author admin
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TenantPlatform implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机器人所属平台表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 机器人所属平台
     */
    private String platformName;

    /**
     * 是否有效 1有效  0无效
     */
    private Boolean isValid;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreateTime;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModifiedTime;


}
