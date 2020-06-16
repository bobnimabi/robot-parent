package com.robot.code.dto;

/**
 * @Author mrt
 * @Date 2020/5/19 18:58
 * @Version 2.0
 */
public interface IResponse<T> {
    public static final int SUCCESS_CODE = 10000;
    public static final String SUCCESS_MES = "操作成功";

    public static final int LOGIN_SUCCESS_CODE = 10001;
    public static final String LOGIN_SUCCESS_MES = "登录成功";

    public static final int FAIL_CODE = 11111;
    public static final String FAIL_MES = "操作失败";
}