package com.clevel.selos.model;

public enum MortgageConfirmedType {
	NA("app.mortgageConfirm.confirm.radio.na"),
	YES("app.mortgageConfirm.confirm.radio.yes"),
	NO("app.mortgageConfirm.confirm.radio.no"),
	CLEAN_LOAN("app.mortgageConfirm.confirm.radio.cleanLoan");
	
	private final String msgKey;
	
	private MortgageConfirmedType(String msgKey) {
		this.msgKey = msgKey;
	}
	
	public String getMsgKey() {
		return msgKey;
	}
	
	public static final MortgageConfirmedType[] displayList() {
		return new MortgageConfirmedType[] {
			YES,NO,CLEAN_LOAN
			};
	}
}
