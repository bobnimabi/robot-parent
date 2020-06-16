package com.robot.core.chain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author mrt
 * @Date 2020/6/16 11:26
 * @Version 2.0
 */
public class InvokerBuilder {
    /**
     * 构造Invoker链
     * @param filters
     * @param <P>
     * @param <R>
     * @return
     */
    public static final <P,R>Invoker buildInvokerChain(List<? extends Filter<P,R>> filters) {
        Invoker last = new Invoker<P,R>() {
            @Override
            public void invoke(P params, R result) throws Exception {}
        };
        if (!filters.isEmpty()) {
            Collections.sort(filters, COMPARATOR);
            for (int i = filters.size() - 1; i >= 0; i--) {
                final Filter<P,R> filter = filters.get(i);
                final Invoker next = last;
                last = new Invoker<P,R>() {
                    @Override
                    public void invoke(P params,R result) throws Exception {
                        filter.dofilter(params, result, next);
                    }
                };
            }
        }
        return last;
    }

    /**
     * 对Invoker进行排序
     */
    private static final Comparator COMPARATOR =new Comparator<Filter>() {
        @Override
        public int compare(Filter o1, Filter o2) {
            return o1.order() - o2.order();
        }
    };
}
