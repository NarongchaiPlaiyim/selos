package com.clevel.selos.model;

public enum RequestAccountType {
	NA("app.account.requestAccountType.radio.na"),
	NEW("app.account.requestAccountType.radio.new"),
	EXISTING("app.account.requestAccountType.radio.exist");
	
	private final String msgKey;
	private RequestAccountType(String msgKey) {
		this.msgKey = msgKey;
	}
	public String getMsgKey() {
		return msgKey;
	}
	public static final RequestAccountType[] displayList() {
		return new RequestAccountType[] {
			NEW,EXISTING
		};
	}
}
