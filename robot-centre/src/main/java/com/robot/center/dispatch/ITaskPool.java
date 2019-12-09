package com.robot.center.dispatch;//package com.bbin.robotWrapper.core.schedue;


import com.robot.center.execute.TaskWrapper;

/**
 * Created by mrt on 2019/7/11 0011 下午 6:35
 */
public interface ITaskPool {

    /**
     * 任务入队
     * 1.会添加时间戳
     * @param taskWrapper
     */
    void taskAdd(TaskWrapper taskWrapper,String externalNo);

    /**
     * 获取队列的大小
     * @return
     */
    long size();

    /**
     * 查看是否有可执行任务
     * @return
     * 返回null的情况
     * 1.时间戳未到时
     * 2.限制时间未解禁
     */
    TaskWrapper taskSkim();

    /**
     * 任务出队
     * 1.会添加限制时间
     */
    TaskWrapper taskGet();
}
