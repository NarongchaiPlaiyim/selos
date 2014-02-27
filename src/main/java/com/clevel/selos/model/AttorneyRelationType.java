package com.clevel.selos.model;

public enum AttorneyRelationType {
    NA("app.appointee.relationship.radio.na"), 
    BORROWER("app.appointee.relationship.radio.borrower"), 
    OTHERS("app.appointee.relationship.radio.other");

    private final String msgKey;
    private AttorneyRelationType(String msgKey){
        this.msgKey = msgKey;
    }
    public String getMsgKey() {
		return msgKey;
	}
    public static final AttorneyRelationType[] displayList() {
    	return new AttorneyRelationType[] {
    			BORROWER, OTHERS
    	};
    }
}
