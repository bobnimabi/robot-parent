package com.robot.center.execute;

import com.robot.center.function.IFunctionEnum;
import com.robot.center.function.ParamWrapper;
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
    // 参数
    private ParamWrapper paramWrapper;
    // 功能执行类名称
    private IFunctionEnum functionEnum;

    // 等待字段
    private String waitField;
    // 执行等待时间,单位：秒,null表示不等待
    private Duration waitTime;


}
