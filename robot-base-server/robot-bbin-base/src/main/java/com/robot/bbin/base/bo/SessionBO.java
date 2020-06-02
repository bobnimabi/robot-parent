package com.robot.bbin.base.bo;

import lombok.Data;

/**
 * Created by mrt on 2019/7/17 0017 下午 12:49
 */
@Data
public class SessionBO {
    private String session_id;
    private Boolean password_reset;
    private Boolean expire;
    private SessionData user;
}
