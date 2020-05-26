package com.robot.core.robot.manager;

import lombok.Data;

/**
 * @Author mrt
 * @Date 2020/5/25 15:33
 * @Version 2.0
 */
public interface ICloudCookie {

    /**
     * 获取Cookie
     * @param robotId
     * @return
     * 返回null:
     *  1.Cookie过期
     *  2.Cookie被删除
     * 情景：
     *  1.执行功能的时候
     */
    RobotWrapper getCookie(long robotId);

    /**
     * 存入Cookie
     * @param robotWrapper
     * @return
     * 情景：
     *  1.登录的时候
     *  2.执行完功能归还机器人的时候
     */
    boolean putCookie(RobotWrapper robotWrapper);
}
