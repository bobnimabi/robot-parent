package com.robot.gpk.base.bo;

import lombok.Data;

/**
 * Created by mrt on 2019/7/17 0017 下午 12:46
 */
@Data
public class GeneralBO extends GpsResponseParent{
    private Object ReturnObject;
    private String ErrorMessage;
    private String Methods;
}
