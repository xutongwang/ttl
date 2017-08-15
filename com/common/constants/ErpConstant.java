package com.common.constants;

public enum ErpConstant {
	FAILED(0,"失败"),
	SUCCESS(1,"成功"),
	INVALID_LENGTH(10001,"非法长度"),
	EMPTY_USERNAME(10101, "用户名不可为空"),
    EMPTY_PASSWORD(10102, "密码不可为空"),
    INVALID_USERNAME(10103, "账号不存在"),
    INVALID_PASSWORD(10104, "密码错误"),
    INVALID_ACCOUNT(10105, "账号被冻结");
	public int code;
	public String message;
	private ErpConstant(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
}