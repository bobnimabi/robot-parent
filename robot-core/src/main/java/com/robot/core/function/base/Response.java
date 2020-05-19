package com.robot.core.function.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author mrt
 * @Date 2020/5/19 18:52
 * @Version 2.0
 */
@Data
@AllArgsConstructor
public class Response<T> implements IResponse{
    private boolean success;
    private int code;
    private String message;
    private T obj;

    public static <T>Response<T> SUCCESS(T t) {
        return new Response<T>(true, SUCCESS_CODE, SUCCESS_MES, t);
    }

    public static <T>Response<T> FAIL(String message) {
        return new Response<T>(false, FAIL_CODE, message, null);
    }
}
