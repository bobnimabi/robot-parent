package com.robot.jiuwu.base.vo;

import lombok.Data;

/**
 * Created by mrt on 2019/12/27 0027 17:00
 */
@Data
public class User {
    private Integer userid;
    private String username;
    private String password;
    private Integer roleid;
    private Integer nullity;
    private String prelogintime;
    private String preloginip;
    private String lastlogintime;
    private String lastloginip;
    private Integer logintimes;
    private Integer isband;
    private String bandip;
    private Integer isassist;
    private Integer alertSwitch;
}
