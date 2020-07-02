package com.robot.jiuwu.base.vo;

import lombok.Data;

/**
 * Created by mrt on 11/15/2019 11:53 AM
 */
@Data
public class ResponseBO {
    private Boolean result;
    private SessionBO data;
    private String response_code;
    private String message;
    private String code;
}
