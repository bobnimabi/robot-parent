package com.robot.core.robot.manager;

import com.robot.code.entity.TenantRobot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created by mrt on 2019/7/5 0005 下午 7:30
 */
/*@Data
@NoArgsConstructor
@AllArgsConstructor*/
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
    private Properties properties;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public RobotWrapper(String idCard, CookieStore cookieStore, Properties properties) {
        this.idCard = idCard;
        this.cookieStore = cookieStore;
        this.properties = properties;
    }
    public RobotWrapper() {

    }
}