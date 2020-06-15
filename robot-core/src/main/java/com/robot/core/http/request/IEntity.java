package com.robot.core.http.request;

/**
 * Created by mrt on 10/18/2019 6:38 PM
 * 定制http请求的body
 */
public interface IEntity<k,v> {
    /**
     * Entity键-值 的添加
     *
     * @param key
     * @param value
     * @return
     */
    IEntity add(k key, v value);

    /**
     * 判断是否为空
     * @return
     */
    boolean isEmpty();

}
