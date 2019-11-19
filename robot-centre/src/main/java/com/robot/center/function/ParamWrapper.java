package com.robot.center.function;

/**
 * Created by mrt on 11/15/2019 12:36 PM
 */
public class ParamWrapper<T> {
    private T obj;
    public ParamWrapper(T obj) {
        this.obj = obj;
    }
    public T getObj() {
        return this.obj;
    }
}
