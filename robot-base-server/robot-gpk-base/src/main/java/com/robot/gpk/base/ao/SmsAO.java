package com.robot.gpk.base.ao;

import lombok.Data;

/**
 * Created by mrt on 2020/5/7 18:18
 * 发短信时用
 */
@Data
public class SmsAO {
    private Long robotId;

    private String smsCode;

}
