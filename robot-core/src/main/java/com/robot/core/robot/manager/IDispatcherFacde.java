package com.robot.core.robot.manager;

import com.robot.core.function.base.IActionEnum;

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
     * 从队列轮询Cookie
     * @return
     */
    RobotWrapper popCookie();

    /**
     * 周期性从队列轮询Cookie
     * @param duration
     * @return
     */
    RobotWrapper getRobotDuration(Duration duration);

    /**
     * 归还机器人
     * @param robotWrapper
     */
    void giveBackCookie(RobotWrapper robotWrapper);

    /**
     *
     */
    int getRobotLimitInterval(IActionEnum actionEnum);
}
