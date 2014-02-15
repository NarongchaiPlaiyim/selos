package com.clevel.selos.model;

public enum RadioValue {
    NOT_SELECTED(0, "NOT SELECT","app.button.notselected"),
    NO(1, "No","app.button.no"),
    YES(2, "Yes","app.button.yes"),
    PASS(3, "Pass","app.button.pass"),
    FAIL(4, "Fail","app.button.fail"),
    NA(5, "N/A","app.button.notselected");

    private final int value;
    private final String shortName;
    private final String msgKey;
    RadioValue(int value, String shortName,String msgKey) {
        this.value = value;
        this.shortName = shortName;
        this.msgKey = msgKey;
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
    public static final RadioValue lookup(int value) {
    	for (RadioValue radio : RadioValue.values()) {
    		if (radio.value == value)
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
