package com.clevel.selos.model;

public enum ApproveResult {
	NA(0,"app.approveType.radio.na"),
	SAME_REQUEST(1,"app.approveResult.radio.same"),
	DIFF_REQUEST(2,"app.approveResult.radio.diff");
	
	private final int value;
	private final String msgKey;
	private ApproveResult(int value,String msgKey) {
		this.value = value;
		this.msgKey = msgKey;
	}
	
	public int value() {
		return value;
	}
	public String getMsgKey() {
		return msgKey;
	}
	public static final ApproveResult lookup(int value) {
		for (ApproveResult result : ApproveResult.values()) {
			if (result.value == value)
				return result;
		}
		return NA;
	}
	public static final ApproveResult[] displayList() {
		return new ApproveResult[] {
			SAME_REQUEST,DIFF_REQUEST
		};
	}
}
