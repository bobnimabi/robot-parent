package com.robot.core.chain;

/**
 * 链式责任链
 * @Author mrt
 * @Date 2020/5/18 19:20
 * @Version 2.0
 * p表示参数类型
 * R表示结果类型
 */
public interface Filter<P,R> {

    /**
     * 执行
     * @param params
     * @param result
     * @param invoker
     * @throws Exception
     */
    void dofilter(P params, R result, Invoker<P, R> invoker) throws Exception;

    /**
     * 排序
     * @return
     */
    int order();
}
