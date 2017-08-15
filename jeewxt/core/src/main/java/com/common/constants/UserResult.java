package com.common.constants;

public class UserResult extends BaseResult{
	
	public UserResult(UserConstant userConstant,Object data){
		super(userConstant.getCode(),userConstant.getMessage(),data);
	}
}