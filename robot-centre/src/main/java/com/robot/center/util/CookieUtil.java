package com.robot.center.util;

import com.robot.center.pool.RobotWrapper;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Date;

/**
 * Created by mrt on 2020/3/12 12:47
 */
public class CookieUtil {
    /**
     * 创建httpclient的cookie
     * @param domain
     * @param name
     * @param value
     * @param path
     * @param expiryDate
     * @param isSecure
     * @param version
     * @return
     */
    public static BasicClientCookie createCookie(String domain, String name, String value,
                                           String path, Date expiryDate, boolean isSecure, int version) {
        BasicClientCookie cookie2 =new BasicClientCookie(name, value);
        cookie2.setDomain(domain);
        cookie2.setPath(path);
        cookie2.setExpiryDate(expiryDate);
        cookie2.setSecure(isSecure);
        cookie2.setVersion(version);
        return cookie2;
    }
}
