package com.robot.center.dispatch;//package com.bbin.robotWrapper.core.schedue;


import com.robot.center.execute.TaskWrapper;

/**
 * Created by mrt on 2019/7/11 0011 下午 6:35
 */
public interface ITaskPool {

    // 任务入队
    void taskAdd(TaskWrapper taskWrapper);

    // 队列大小
    long size();

    /**
     * 查看是否有可执行任务
     * @return
     */
    TaskWrapper taskSkim();

    // 任务出队
    TaskWrapper taskGet();
}
