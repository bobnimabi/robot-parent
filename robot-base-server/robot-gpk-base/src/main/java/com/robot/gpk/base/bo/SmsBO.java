package com.robot.gpk.base.bo;

import lombok.Data;

/**
 * Created by mrt on 2020/5/7 18:18
 */
@Data
public class SmsBO extends GeneralBO {
    private String WaitingTime;
    private String SendAddress;
}
