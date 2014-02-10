package com.clevel.selos.model;

public enum RadioValue {
    NOT_SELECTED(0, "NOT SELECT"),
    NO(1, "No"),
    YES(2, "Yes"),
    PASS(3, "Pass"),
    FAIL(4, "Fail"),
    NA(5, "N/A");

    int value;
    String shortName;

    RadioValue(int value, String shortName) {
        this.value = value;
        this.shortName = shortName;
    }

    public int value() {
        return this.value;
    }

    public String shortName() {
        return this.shortName;
    }
    
    public static final RadioValue lookup(int value) {
    	for (RadioValue radio : RadioValue.values()) {
    		if (radio.value == value)
    			return radio;
    	}
    	return NOT_SELECTED;
    }
}
