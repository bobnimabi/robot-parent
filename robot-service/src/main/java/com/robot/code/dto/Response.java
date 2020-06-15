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

    private static final Response SUCCESS = new Response(true, SUCCESS_CODE, SUCCESS_MES, null);
    private static final Response FAILER = new Response(true, SUCCESS_CODE, SUCCESS_MES, null);

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

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Response<T> setObj(T obj) {
        this.obj = obj;
        return this;
    }

    public T getObj() {
        return this.obj;
    }

    public boolean isSuccess() {
        return this.success;
    }


    public static Response SUCCESS() {
        return SUCCESS.clone();
    }

    public static <T> Response<T> SUCCESS(T t) {
        Response<T> clone = (Response<T>) SUCCESS.clone();
        return clone.setObj(t);
    }

    public static <T> Response<T> LOGIN_SUCCESS() {
        return new Response<T>(true, LOGIN_SUCCESS_CODE, LOGIN_SUCCESS_MES, null);
    }

    public static <T> Response<T> FAIL(String message) {
        Response<T> clone = (Response<T>) FAILER.clone();
        return clone.setMessage(message);
    }

    @Override
    protected Response<T> clone() {
        try {
            return (Response<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("克隆Response失败");
    }
}
