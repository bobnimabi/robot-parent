package com.robot.code.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 路径表 
1-100 OG 
101-200 bbin 
201-300 gpk 
301-400棋牌
 * </p>
 *
 * @author admin
 * @since 2020-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TenantRobotPath implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 所属平台的id  tenant_platform表的id
     */
    private Long platformId;

    /**
     * 域名等级，默认1，同一租户下可能出现多个域名,比如二级域名
     */
    private Integer domainRank;

    /**
     * 路径编码（代码靠此来寻找path）
     */
    private String pathCode;

    /**
     * path
     */
    private String path;

    /**
     * 请求方式，枚举
     */
    private String method;

    /**
     * 备注
     */
    private String memo;

    /**
     * 请求配置id，http_request_config表的id
     */
    private Long httpRequestConfigId;

    /**
     * 异步请求配置id，aysnc_request_config表的id
     */
    private Long asyncRequestConfigId;

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
