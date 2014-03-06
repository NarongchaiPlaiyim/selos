package com.clevel.selos.model;

public enum PerfectReviewType {
	NA("app.perfectionReview.confirmType.type.na"),
	CONTRACT("app.perfectionReview.confirmType.type.contract"),
	PLEDGE("app.perfectionReview.confirmType.type.pledge"),
	MORTGAGE("app.perfectionReview.confirmType.type.mortgage"),
	TCG("app.perfectionReview.confirmType.type.tcg"),
	FEE_COLLECTION("app.perfectionReview.confirmType.type.feeCollection"),
	CUSTOMER("app.perfectionReview.confirmType.type.customer"),
	ACCOUNT("app.perfectionReview.confirmType.type.account")
	;
	private final String msgKey;
	private PerfectReviewType(String msgKey) {
		this.msgKey = msgKey;
	}
	public String getMsgKey() {
		return msgKey;
	}
	
	public static final PerfectReviewType[] displayList() {
		return new PerfectReviewType[] {
			CONTRACT,PLEDGE,MORTGAGE,TCG,FEE_COLLECTION,CUSTOMER,ACCOUNT
		};
	}
}
