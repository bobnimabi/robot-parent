package com.robot.gpk.base.dto;

import com.robot.gpk.base.vo.GpsResponseParent;
import lombok.Data;

/**
 * Created by mrt on 2020/5/7 18:18
 * 发短信时用
 */
@Data
public class SmsDTO {
    private Long robotId;

    private String smsCode;

}
