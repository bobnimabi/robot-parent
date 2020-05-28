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
 * 路径表
 * </p>
 *
 * @author admin
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TenantRobotPath implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属平台的id  tenant_platform表的id
     */
    private Long platformId;

    /**
     * 域名等级，默认1，同一租户下可能出现多个域名,比如二级域名
     */
    private Integer rank;

    /**
     * 域名等级
     */
    private String actionCode;

    /**
     * path
     */
    private String path;

    /**
     * 请求方式，1.get 2.post表单提交 3.post的JSON 4.post的文件上传
     */
    private String method;

    /**
     * path解释
     */
    private String memo;

    /**
     * 请求配置，http_request_config表的id
     */
    private Long httpRequestConfigId;

    /**
     * 同一机器人请求限制，单位：秒，0表示无限制
     */
    private Integer robotTimeLimit;

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
