package com.robot.bbin.base.vo;

import lombok.Data;

/**
 * Created by mrt on 2019/7/17 0017 下午 12:49
 */
@Data
public class SessionVO {
    private String session_id;
    private Boolean password_reset;
    private Boolean expire;
    private UserVO user;
}
