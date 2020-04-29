package com.robot.center.pool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Date;

/**
 * Created by mrt on 2020/4/3 12:52
 */
@Service
public class CookieManager {

    @Autowired
    private StringRedisTemplate redis;

    private static RobotWrapper deserializationRobot(String robotJson) {
        if (StringUtils.isEmpty(robotJson)) {
            return null;
        }
        RobotWrapper robotWrapper = JSON.parseObject(robotJson, RobotWrapper.class);

        JSONArray cookies = JSON.parseObject(robotJson).getJSONObject("cookieStore").getJSONArray("cookies");
        CookieStore cookieStoreNew = new BasicCookieStore();
        for (int i = 0; i < cookies.size(); i++) {
            JSONObject jsonObject = cookies.getJSONObject(i);
            String name = jsonObject.getString("name");
            String value = jsonObject.getString("value");
            String domain = jsonObject.getString("domain");
            String path = jsonObject.getString("path");
            Boolean secure = jsonObject.getBoolean("secure");
            String expiryDate = jsonObject.getString("expiryDate");
            String creationDate = jsonObject.getString("creationDate");
            Integer version = jsonObject.getInteger("version");

            BasicClientCookie cookie = new BasicClientCookie(name, value);
            cookie.setDomain(domain);
            cookie.setPath(path);
            if (null != expiryDate) {
                cookie.setExpiryDate(new Date(Long.parseLong(expiryDate)));
            }
            if (null != creationDate) {
                cookie.setCreationDate(new Date(Long.parseLong(creationDate)));
            }
            if (null != secure) {
                cookie.setSecure(secure);
            }
            if (null != version) {
                cookie.setVersion(version);
            }

            cookieStoreNew.addCookie(cookie);
        }
        robotWrapper.setCookieStore(cookieStoreNew);
        return robotWrapper;
    }
}
