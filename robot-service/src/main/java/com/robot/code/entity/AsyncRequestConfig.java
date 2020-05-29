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
 * @since 2020-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AsyncRequestConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 机器人限制时间，单位：秒 ， 表示该接口同一个机器人几秒可以调一次
     */
    private Integer robotTimeLimit;

    /**
     * 域名称（接口DTO里面的属性名称，用来做限制）
     */
    private String field;

    /**
     * 域限制时间，单位：秒
     */
    private Integer fieldTime;

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
