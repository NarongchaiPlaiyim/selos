package com.clevel.selos.model;

public enum ApproveType {
	NA(0,"app.approveType.radio.na"),
	NEW(1,"app.approveType.radio.new"),
	NEW_CHANGE(2,"app.approveType.radio.newChange");
	
	private final int value;
	private final String msgKey;
	private ApproveType(int value,String msgKey) {
		this.value = value;
		this.msgKey = msgKey;
	}
	
	public int value() {
		return value;
	}
	public String getMsgKey() {
		return msgKey;
	}
	public static final ApproveType lookup(int value) {
		for (ApproveType type : ApproveType.values()) {
			if (type.value == value)
				return type;
		}
		return NA;
	}
	
	public static final ApproveType[] displayList() {
		return new ApproveType[] {
			NEW,NEW_CHANGE
		};
	}
	 
}
