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
 * @since 2020-04-01
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
     * 动作编码
     */
    private String code;

    /**
     * path
     */
    private String path;

    /**
     * 请求方式，1.get 2.post表单提交 3.post的JSON 4.post的文件上传
     */
    private Integer method;

    /**
     * path解释
     */
    private String memo;

    /**
     * tenant_robot_path_config表的id
     */
    private Long pathConfigId;

    /**
     * http_request_config表的id
     */
    private Long requestConfigId;

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
