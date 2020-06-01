package com.robot.core.function.base;

import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.client.ResponseHandler;

import javax.validation.constraints.NotNull;

/**
 * @Author mrt
 * @Date 2020/5/19 19:34
 * @Version 2.0
 */
@Data
@AllArgsConstructor
public class FunctionProperty {

    /**
     * 路径编码枚举
     */
    @NotNull
    private IPathEnum pathEnum;

    /**
     * 请求头
     */
    private CustomHeaders headers;

    /**
     * 请求体
     */
    private ICustomEntity entity;

    /**
     * 是否检查掉线，默认：true
     */
    @NotNull
    private ICheckLost checkLost;

    /**
     * http响应处理器
     */
    private ResponseHandler<StanderHttpResponse> responseHandler;

    /**
     * 结果处理器
     */
    @NotNull
    private IResultHandler resultHandler;

    /**
     * 机器人包装类
     */
    @NotNull
    private RobotWrapper robotWrapper;

    /**
     * 外部订单号
     */
    private String exteralNo;

    /**
     * 流水id
     */
    @NotNull
    private long recordId;
}
