package com.robot.center.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 2020/1/9 0009 18:08
 * 责任链模式
 */
public abstract class FilterChainBase<T> implements FilterChain<T> {

    private List<Invoker> filterChains;

    @Override
    public void doChain(List<T> rows) throws Exception {
        this.doChain(0, rows);
    }

    public void doChain(int pos, List<T> rows) throws Exception {
        if (pos < filterChains.size() && !CollectionUtils.isEmpty(rows)) {
            Invoker invoker = filterChains.get(pos++);
            invoker.doFilter(pos,rows,this);
        }
    }

    public void setInvoker(Invoker invoker) {
        if (null == filterChains) {
            filterChains = new ArrayList<>(10);
        }
        filterChains.add(invoker);
    }

    /**
     * 调用者
     */
    public static abstract class Invoker<T>{
        private void doFilter(int pos, List<T> rows, FilterChainBase filterChain) throws Exception {
            before(pos, rows);
            filterChain.doChain(pos, rows);
            after(pos, rows);
        }
        protected abstract void before(int pos, List<T> rows) throws Exception;
        protected abstract void after(int pos, List<T> rows);
    }
}
