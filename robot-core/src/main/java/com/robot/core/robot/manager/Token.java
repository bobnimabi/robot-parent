package com.robot.core.robot.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author mrt
 * @Date 2020/5/25 17:09
 * @Version 2.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token implements Serializable {

    /**
     * 机器人id
     */
    private Long robotId;

    /**
     * 身份标识
     */
    private String idCard ;
}
