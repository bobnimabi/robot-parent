package com.robot.code.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 响应流水
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ResponseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 流水的id
     */
    private Long requestRecordId;

    /**
     * 转换后的响应内容
     */
    private String parsedContent;

    /**
     * 相对地址（原始响应内容压缩包）
     */
    private String originalContent;

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
