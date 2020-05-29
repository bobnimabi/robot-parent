package com.robot.core.task.dispatcher;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author mrt
 * @Date 2020/5/28 13:50
 * @Version 2.0
 */
public interface ITaskPool {
    /**
     * 任务入队
     * 1.会添加时间戳
     * @param taskWrapper
     */
    void taskAdd(TaskWrapper taskWrapper,String externalNo) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException;

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
