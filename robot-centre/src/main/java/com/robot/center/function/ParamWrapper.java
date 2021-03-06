package com.robot.center.function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by mrt on 11/15/2019 12:36 PM
 */
@Data
@NoArgsConstructor
public class ParamWrapper<T> implements Serializable {
    private T obj;
    public ParamWrapper(T obj) {
        this.obj = obj;
    }
    public T getObj() {
        return this.obj;
    }

}
