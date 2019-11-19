package com.robot.code.entity;

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
public class TenantRobotRespLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 流水的id
     */
    private String recordId;

    /**
     * 原生响应内容
     */
    private String respContent;

    /**
     * 是否有效 1有效  0无效
     */
    private Boolean isValid;

    private LocalDateTime gmtCreateTime;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModifiedTime;


}
