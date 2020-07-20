package com.robot.og.base.bo;

import lombok.Data;

/**
 * <p>
 * 打款响应结果
 * </p>
 *
 * @author tanke
 * @date 2020/7/10
 */
@Data
public class PayBO {
	private boolean success;
	private String message;
	private int data;
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean getSuccess() {
		return success;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}

	public void setData(int data) {
		this.data = data;
	}
	public int getData() {
		return data;
	}

}
