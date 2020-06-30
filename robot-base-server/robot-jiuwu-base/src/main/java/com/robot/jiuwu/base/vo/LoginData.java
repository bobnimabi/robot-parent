package com.robot.jiuwu.base.vo;

import lombok.Data;

/**
 * Created by mrt on 2019/12/27 0027 17:00
 */
@Data
public class LoginData{
    private Roles roles;
    private User user;
    private String token;
}
