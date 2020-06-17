package com.robot.core.robot.manager;

/**
 * @Author mrt
 * @Date 2020/5/27 12:06
 * @Version 2.0
 */
public interface IExecutorFacde {



    /**
     * 下线机器人
     * @param robotId
     * @return
     */
    boolean offline(long robotId);
}
