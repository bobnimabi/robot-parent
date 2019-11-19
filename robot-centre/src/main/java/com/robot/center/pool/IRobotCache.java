package com.robot.center.pool;

/**
 * Created by mrt on 2019/7/5 0005 下午 11:19
 */
public interface IRobotCache {

    /**
     * 获取机器人
     * @return
     * 返回null的情况
     * 1.队列里面没有机器人
     * 2.机器人过期（已被删除）
     */
    RobotWrapper cacheRobotGet();

    /**
     * 归还机器人
     * @param robot
     */
    void cacheGiveBack(RobotWrapper robot);

}
