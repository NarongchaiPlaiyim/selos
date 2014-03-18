package com.clevel.selos.integration.brms.model;

public enum BRMSYesNo {
    YES("Y", true), NO("N", false);

    private final String value;
    private final boolean realValue;

    private BRMSYesNo(String value, boolean realValue){
        this.value = value;
        this.realValue = realValue;
    }

    public static final BRMSYesNo lookup(boolean value) {
        for (BRMSYesNo type : BRMSYesNo.values()) {
            if (type.realValue == value)
                return type;
        }
        return NO;
    }

    public String value(){
        return value;
    }
}
