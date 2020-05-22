package com.robot.core.chain;

/**
 * @Author mrt
 * @Date 2020/5/18 19:20
 * @Version 2.0
 * p表示参数类型
 * R表示结果类型
 */
public interface Filter<P,R> {
    /**
     * 执行filter
     *
     * @param params  参数
     * @param invoker
     * @return
     * @throws Exception
     */
    R dofilter(P params, Invoker<P, R> invoker) throws Exception;

    /**
     * 排序
     * @return
     */
    int order();
}
