package com.robot.center.chain;

import java.util.List;

/**
 * Created by mrt on 2020/1/9 0009 18:01
 */
public interface FilterChain<T> {
    void doChain(List<T> rows) throws Exception;
}
