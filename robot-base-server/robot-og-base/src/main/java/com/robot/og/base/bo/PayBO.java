package com.robot.og.base.bo;

/**
 * <p>
 * 打款响应结果
 * </p>
 *
 * @author tanke
 * @date 2020/7/10
 */
public class PayBO {
	private String code;
	private String msg;
	private String id;
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
}
