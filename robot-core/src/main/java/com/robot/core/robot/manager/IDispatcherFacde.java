package com.robot.core.robot.manager;

import java.time.Duration;

/**
 * @Author mrt
 * @Date 2020/5/27 12:06
 * @Version 2.0
 */
public interface IDispatcherFacde {
    /**
     * 指定获取Cookie
     * @param robotId
     * @return
     */
    RobotWrapper getCookie(long robotId);

    /**
     * 产生新CookieStore
     * @param robotId
     */
    void newCookie(long robotId);

    /**
     * 上线机器人
     * @param robotWrapper
     * @return
     * 注意：只允许登录成功后调用
     */
    boolean online(RobotWrapper robotWrapper);

    /**
     * 从队列轮询Cookie
     * @return
     */
    RobotWrapper popCookie();

    /**
     * 周期性从队列轮询Cookie
     * @param duration
     * @return
     */
    RobotWrapper getCookieDuration(Duration duration);

    /**
     * 归还机器人
     * @param robotWrapper
     */
    void giveBackCookieAndToken(RobotWrapper robotWrapper);


    /**
     * 归还机器人：登录相关接口使用
     * @param robotWrapper
     */
    void giveBackCookie(RobotWrapper robotWrapper);
}
