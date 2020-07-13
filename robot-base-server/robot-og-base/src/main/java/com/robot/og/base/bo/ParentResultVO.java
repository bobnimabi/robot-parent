package com.robot.og.base.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrt on 2019/12/27 0027 17:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentResultVO<T> {
    private String code;
    private String msg;
    private String id;
    private T data;
}
