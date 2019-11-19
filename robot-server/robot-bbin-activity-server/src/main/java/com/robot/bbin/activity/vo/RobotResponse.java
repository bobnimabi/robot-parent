package com.robot.bbin.activity.vo;

import lombok.Data;

/**
 * Created by mrt on 11/15/2019 11:53 AM
 */
@Data
public class RobotResponse {
    private Boolean result;
    private SessionVO data;
    private String response_code;
    private String message;
    private String code;
}
