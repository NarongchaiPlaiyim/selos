package com.clevel.selos.model;

public enum CollateralCreditType {
	NA("app.collateral.creditType.radio.na"),
	NEW("app.collateral.creditType.radio.new"),
	EXIST("app.collateral.creditType.radio.exist")
	;
	
	private String msgKey;
	private CollateralCreditType(String msgKey) {
		this.msgKey = msgKey;
	}
	public String getMsgKey() {
		return msgKey;
	}
}
