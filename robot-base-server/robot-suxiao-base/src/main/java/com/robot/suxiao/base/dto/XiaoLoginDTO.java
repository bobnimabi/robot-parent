package com.robot.suxiao.base.dto;

import lombok.Data;

/**
 * Created by mrt on 2020/3/7 13:47
 */
@Data
public class XiaoLoginDTO {
    private String username;
    private String password;
    private String phone;
    private String code;
    private String remember_me;
    private String _csrf;
    private String type;
    private String loginType;
    private String platform;
}
