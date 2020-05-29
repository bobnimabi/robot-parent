package com.robot.core.task.dispatcher;

import com.robot.code.entity.AsyncRequestConfig;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.ParamWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;

/**
 * Created by mrt on 2019/7/10 0010 下午 5:05
 * 任务包装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskWrapper implements Serializable {

    /**
     * 参数
      */
    private ParamWrapper paramWrapper;

    /**
     * 功能执行类名称
      */
    private IFunctionEnum functionEnum;

    /**
     * 异步请求配置
     */
    AsyncRequestConfig config;
}
