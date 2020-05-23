package com.robot.core.function.base;


import com.robot.core.http.response.StanderHttpResponse;

/**
 * Created by mrt on 10/28/2019 10:20 AM
 * T原始响应数据类型
 * E转换后数据类型
 */
public interface IResultParse<T,E> {
    /**
     * 将响应的html、json等转换成对象，还要判断响应的成功或失败
     * 原因：html有时不能转换是因为某个失败的原因，所以只能将转换和判断放一起
     * @param response
     * @return
     */
    Response<E> parse2Obj(StanderHttpResponse<T,E> response);
}
