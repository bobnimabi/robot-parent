package com.robot.gpk.base.vo;

import lombok.Data;

/**
 * Created by mrt on 2019/7/17 0017 下午 12:46
 */
@Data
public class GeneralVO extends GpsResponseParent{
    private Object ReturnObject;
    private String ErrorMessage;
    private String Methods;
}
