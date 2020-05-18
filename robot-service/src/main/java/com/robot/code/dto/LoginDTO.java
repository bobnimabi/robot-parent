package com.robot.code.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 机器人表
 * </p>
 *
 * @author admin
 * @since 2019-10-24
 */
@Data
public class LoginDTO {
    /**
     * 机器人id
     */
    private Long id;

    /**
     * OPT
     */
    private String opt;

    /**
     * 图片验证码
     */
    private String imageCode;
}
