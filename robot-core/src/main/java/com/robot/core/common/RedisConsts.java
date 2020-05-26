package com.robot.core.common;

/**
 * @Author mrt
 * @Date 2020/5/25 18:56
 * @Version 2.0
 */
public class RedisConsts {
    /**
     * 项目
     * 8
     */
    public static final String PROJECT = "S_ROBOT:";

    /**
     * Redis：机器人ID_CARD标志
     * 8
     */
    public static final String ID_CARD = PROJECT + "ID_CARD:";

    /**
     * Redis：机器人COOKIE标志
     * 7
     */
    public static final String COOKIE = PROJECT + "COOKIE:";

    /**
     * Redis：TOKEN队列标志
     * 12
     */
    public static final String TOKEN_QUEUE = PROJECT + "TOKEN_QUEUE:";
}
