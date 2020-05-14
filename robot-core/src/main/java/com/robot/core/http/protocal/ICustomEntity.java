package com.robot.core.http.protocal;

/**
 * Created by mrt on 10/18/2019 6:38 PM
 * 定制http请求的body
 */
public interface ICustomEntity<T> {
    /**
     * Entity键-值 的添加
     * @param key
     * @param value
     * @return
     */
    ICustomEntity add(String key, T value);

    String toString();
}
