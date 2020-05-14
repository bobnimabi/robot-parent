package com.robot.core.http.schema;

/**
 * Created by mrt on 10/18/2019 6:38 PM
 * 定制http请求的body
 */
public interface ICustomEntity {
    /**
     * Entity键-值 的添加
     * @param key
     * @param value
     * @return
     */
    ICustomEntity add(String key, String value);

    String toString();
}
