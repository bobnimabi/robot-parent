package com.robot.suxiao.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrt on 2020/3/7 14:32
 * 登录前获取下登录相关参数，比如csrf
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {
    private String code;
	private String message;
}
