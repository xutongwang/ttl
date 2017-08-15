package com.common.constants;

public class ErpResult extends BaseResult {

	public ErpResult(ErpConstant erpConstant, Object data) {
		super(erpConstant.getCode(), erpConstant.getMessage(), data);
	}
}