package com.clevel.selos.model;

public enum FeeAccountType {
	NA("app.feeAccountType.radio.na"),
	NEW_OD("app.feeAccountType.radio.newOD") ,
	EXCEED("app.feeAccountType.radio.exceed")
	;
	private final String msgKey;
	private FeeAccountType(String msgKey) {
		this.msgKey = msgKey;
	}
	
	public String getMsgKey() {
		return msgKey;
	}
}
