package com.robot.core.chain;

/**
 * @Author mrt
 * @Date 2020/5/18 19:20
 * @Version 2.0
 */
public abstract class Filter<T> {
    public abstract boolean dofilter(T invocation) throws Exception;

    public abstract int order();

    boolean doFilterFinal(Invoker invoker, T invocation) throws Exception {
        return dofilter(invocation) ? (null != invoker ? invoker.invoke(invocation) : true) : false;
    }
}
