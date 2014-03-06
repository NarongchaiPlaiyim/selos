package com.clevel.selos.model;

public enum PerfectReviewStatus {
	NA("app.perfectionReview.status.type.na"),
	ON_HAND("app.perfectionReview.status.type.onhand"),
	COMPLETE("app.perfectionReview.status.type.complete")
	;
	private final String msgKey;
	private PerfectReviewStatus(String msgKey) {
		this.msgKey = msgKey;
	}
	public String getMsgKey() {
		return msgKey;
	}
}
