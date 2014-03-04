package com.clevel.selos.model;

public enum ConfirmAccountType {
	NA("app.account.confirmAccountType.radio.na"),
	OPEN("app.account.confirmAccountType.radio.open"),
	NOT_OPEN("app.account.confirmAccountType.radio.notopen");
	
	private final String msgKey;
	private ConfirmAccountType(String msgKey) {
		this.msgKey = msgKey;
	}
	public String getMsgKey() {
		return msgKey;
	}
	public static final ConfirmAccountType[] displayList() {
		return new ConfirmAccountType[] {
			OPEN,NOT_OPEN
		};
	}
}
