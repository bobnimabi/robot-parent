package com.robot.bbin.activity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 机器人表
 * </p>
 *
 * @author admin
 * @since 2019-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderNoQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    //账号
    private String userName;
    //起始时间
    private LocalDateTime startDate;
    //终止时间
    private LocalDateTime endDate;
    //平台编码
    private String gameCode;
    //注单号
    private String orderNo;
    //游戏编码
    private List<GameChild> children;

    // 参数：是否是BBIN
    private Boolean is_BBIN;

    /**
     * 携带的参数
      */
    // 局查询需要携带的参数
    private String barId;
    // 消消除窗口信息需要携带的参数
    private String pageId;
    private String key;
    private String gameType;//游戏编码
}
