package com.robot.core.robot.manager;

/**
 * @Author mrt
 * @Date 2020/5/25 15:33
 * @Version 2.0
 */
public interface ICloudTokenQueue {

    /**
     * 弹出Token
     * @return
     * 返回null的情况
     * 1.队列里面没有机器人
     * 2.队列里的机器人都过期
     */
    Token popToken();

    /**
     * 归还或新增Token
     * @param token
     */
    boolean pushToken(Token token);

}
