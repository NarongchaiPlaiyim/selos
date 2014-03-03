package com.clevel.selos.model;

public enum RadioValue {
    NOT_SELECTED(0, "NOT SELECT","app.button.notselected", false),
    NO(1, "No","app.button.no", false),
    YES(2, "Yes","app.button.yes", true),
    PASS(3, "Pass","app.button.pass", true),
    FAIL(4, "Fail","app.button.fail", false),
    NA(5, "N/A","app.button.notselected", false);

    private final int value;
    private final String shortName;
    private final String msgKey;
    private final boolean boolValue;

    RadioValue(int value, String shortName,String msgKey, boolean boolValue) {
        this.value = value;
        this.shortName = shortName;
        this.msgKey = msgKey;
        this.boolValue = boolValue;
    }

    public int value() {
        return this.value;
    }

    public String shortName() {
        return this.shortName;
    }
    public String getMsgKey() {
		return msgKey;
	}
    public boolean getBoolValue(){
        return boolValue;
    }
    public static final RadioValue lookup(int value) {
    	for (RadioValue radio : RadioValue.values()) {
    		if (radio.ordinal() == value)
    			return radio;
    	}
    	return NOT_SELECTED;
    }
    
    public static final RadioValue[] displayListYesNo() {
    	return new RadioValue[] {
    			YES,NO
    		};
    }
}
