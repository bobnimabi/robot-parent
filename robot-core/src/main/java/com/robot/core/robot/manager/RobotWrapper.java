package com.robot.core.robot.manager;

import com.robot.code.entity.TenantRobot;
import lombok.Data;
import org.apache.http.client.CookieStore;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created by mrt on 2019/7/5 0005 下午 7:30
 */
@Data
public class RobotWrapper extends TenantRobot implements Serializable {
    /**
     * 身份标识，用于携带Token的IdCard
     * 自身并不需要IdCard
      */
    private String idCard;

    /**
     * CookieStore
     */
    private CookieStore cookieStore;

    /**
     * 存放属性
     */
    private Properties properties = new Properties();


}