package com.robot.center.function;

import java.io.Serializable;

/**
 * Created by mrt on 11/15/2019 12:36 PM
 */
public class ParamWrapper<T> implements Serializable {
    private T obj;
    public ParamWrapper(T obj) {
        this.obj = obj;
    }
    public T getObj() {
        return this.obj;
    }
}
