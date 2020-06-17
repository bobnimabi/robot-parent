package com.robot.code.response;

/**
 * @Author mrt
 * @Date 2020/5/19 18:52
 * @Version 2.0
 */
public class Response<T> implements Cloneable {
    private boolean success;
    private int code;
    private String message;
    private T obj;

    // 通用成功或失败
    private static final Response SUCCESS = new Response(ResponseEnum.GENERAL_SUCCESS, null);
    private static final Response FAILER = new Response(ResponseEnum.GENERAL_FAILER , null);

    private Response(ResponseEnum responseEnum,T obj){
        this.success = responseEnum.isSuccess();
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
        this.obj = obj;
    }

    public String getMessage() {
        return this.message;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getCode() {
        return this.code;
    }
    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public Response<T> setObj(T obj) {
        this.obj = obj;
        return this;
    }
    public T getObj() {
        return this.obj;
    }

    public Response setSuccess(boolean isSuccess) {
        this.success = isSuccess;
        return this;
    }
    public boolean isSuccess() {
        return this.success;
    }

    public static Response SUCCESS() {
        return SUCCESS.clone();
    }

    public static Response SUCCESS(ResponseEnum e) {
        Response clone =  SUCCESS.clone();
        return create(clone, e, null);
    }

    public static <T> Response<T> SUCCESS(T t) {
        Response<T> clone = (Response<T>) SUCCESS.clone();
        return clone.setObj(t);
    }

    public static <T> Response<T> SUCCESS(ResponseEnum e, T t) {
        Response<T> clone = (Response<T>) SUCCESS.clone();
        return create(clone, e, t);
    }

    public static <T> Response<T> FAIL() {
        return FAILER.clone();
    }

    public static <T> Response<T> FAIL(String message) {
        Response<T> clone = (Response<T>) FAILER.clone();
        return clone.setMessage(message);
    }

    public static <T> Response<T> FAIL(String message,T t) {
        Response<T> clone = (Response<T>) FAILER.clone();
        return clone.setMessage(message).setObj(t);
    }

    public static <T> Response<T> FAIL(ResponseEnum e) {
        Response<T> clone = (Response<T>) FAILER.clone();
        return create(clone, e, null);
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

    private static Response create(Response r,ResponseEnum e,Object t) {
        return r.setObj(t).setCode(e.getCode()).setMessage(e.getMessage()).setSuccess(e.isSuccess());
    }
}
