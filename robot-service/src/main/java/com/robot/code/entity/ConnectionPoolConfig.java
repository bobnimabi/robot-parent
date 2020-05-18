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
 * httpclient的连接池配置
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConnectionPoolConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 连接池最大连接数
     */
    private Integer maxTotal;

    /**
     * 默认路由并发数
     */
    private Integer maxPerRoute;

    /**
     * 无效连接校验时间，单位：秒
     */
    private Integer validateAfterInactivity;

    /**
     * 空闲和过期连接检查时间间隔，单位：秒
     */
    private Integer sleepTime;

    /**
     *  最大空闲时间，单位：秒
     */
    private Integer maxIdleTime;

    /**
     * 注释
     */
    private String memo;

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
