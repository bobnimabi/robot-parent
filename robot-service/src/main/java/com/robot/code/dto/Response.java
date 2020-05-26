package com.robot.code.dto;

/**
 * @Author mrt
 * @Date 2020/5/19 18:52
 * @Version 2.0
 */
public class Response<T> implements IResponse<T>, Cloneable {
    private boolean success;
    private int code;
    private String message;
    private T obj;

    private Response(boolean isSuccess,int code ,String message,T obj){
        this.success = isSuccess;
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Response<T> setObj(T obj) {
        this.obj = obj;
        return this;
    }

    private static final Response SUCCESS = new Response(true, SUCCESS_CODE, SUCCESS_MES, null);

    private static final Response FAILER = new Response(true, SUCCESS_CODE, SUCCESS_MES, null);

    public static Response SUCCESS() {
        try {
            Response clone = SUCCESS.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Response(false, FAIL_CODE, "响应封装异常", null);
    }

    public static <T> Response<T> SUCCESS(T t) {
        try {
            Response<T> clone = (Response<T>) SUCCESS.clone();
            return clone.setObj(t);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Response<T>(false, FAIL_CODE, "响应封装异常", t);
    }

    public static <T> Response<T> FAIL(String message) {
        try {
            Response<T> clone = (Response<T>) FAILER.clone();
            clone.setMessage(message);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Response<T>(false, FAIL_CODE, "响应封装异常", null);
    }

    @Override
    protected Response<T> clone() throws CloneNotSupportedException {
        return (Response<T>) super.clone();
    }
}
