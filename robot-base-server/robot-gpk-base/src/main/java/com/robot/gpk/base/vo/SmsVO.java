package com.robot.gpk.base.vo;

import lombok.Data;

/**
 * Created by mrt on 2020/5/7 18:18
 */
@Data
public class SmsVO extends GeneralVO{
    private String WaitingTime;
    private String SendAddress;
}
