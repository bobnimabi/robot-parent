package com.robot.center.pool;

import com.robot.code.entity.TenantRobot;
import lombok.Data;
import org.apache.http.client.CookieStore;

import java.io.Serializable;

/**
 * Created by mrt on 2019/7/5 0005 下午 7:30
 */
@Data
public class RobotWrapper extends TenantRobot implements Serializable {
    // 身份标识
    private String idCard ;
    // CookieStore
    private CookieStore cookieStore;
}