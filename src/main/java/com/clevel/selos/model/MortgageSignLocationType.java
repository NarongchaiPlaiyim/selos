package com.clevel.selos.model;

public enum MortgageSignLocationType {
	NA("app.mortgageSummary.agreement.signContract.radio.na"),
	BRANCH("app.mortgageSummary.agreement.signContract.radio.branch"),
	ZONE("app.mortgageSummary.agreement.signContract.radio.zone");
	
	private final String msgKey;
	
	private MortgageSignLocationType(String msgKey) {
		this.msgKey = msgKey;
	}
	
	public String getMsgKey() {
		return msgKey;
	}
	
	public static final MortgageSignLocationType[] displayList() {
		return new MortgageSignLocationType[] {
			BRANCH,ZONE	
			};
	}
}
