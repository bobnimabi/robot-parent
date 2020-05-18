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
 * 请求配置（专业人员）
1.只为path进行配置
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HttpRequestConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 是否重定向 1是  0否
     */
    private Boolean isRedirect;

    /**
     * 从连接池获取连接超时，单位：秒
     */
    private Integer connectRequestTimeout;

    /**
     * 建立连接超时，单位：秒
     */
    private Integer connectTimeout;

    /**
     * 响应超时，单位：秒
     */
    private Integer responseTimeout;

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
