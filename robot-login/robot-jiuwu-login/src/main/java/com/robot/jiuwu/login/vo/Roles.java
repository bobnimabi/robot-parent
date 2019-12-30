package com.robot.jiuwu.login.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by mrt on 2019/12/27 0027 17:00
 */
@Data
public class Roles {
    @JSONField(name="RoleName")
    private String roleName;
    @JSONField(name="Description")
    private String description;
    @JSONField(name="RoleID")
    private String roleID;
}
