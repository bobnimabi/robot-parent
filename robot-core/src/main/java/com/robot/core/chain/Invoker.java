package com.robot.core.chain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 链式责任链
 * @Author mrt
 * @Date 2020/5/18 19:20
 * @Version 2.0
 */
public interface Invoker<P,R> {
    void invoke(P params,R result) throws Exception;
}
