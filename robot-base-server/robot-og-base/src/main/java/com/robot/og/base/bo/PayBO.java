package com.robot.og.base.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 打款响应结果
 * </p>
 *
 * @author tanke
 * @date 2020/7/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayBO {
	private Boolean success;
	private String message;
	private Integer data;
	//机器人订单号
	private String robotRecordNo;
	//外部订单号
	private String outPayNo;

}
