package com.robot.core.function.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口请求参数包装类
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
