package com.clevel.selos.model;

public enum BRMSYesNo {
    YES("Y", true), NO("N", false);

    private final String value;
    private final boolean boolValue;

    private BRMSYesNo(String value, boolean boolValue){
        this.value = value;
        this.boolValue = boolValue;
    }

    public static final BRMSYesNo lookup(boolean value) {
        for (BRMSYesNo type : BRMSYesNo.values()) {
            if (type.boolValue == value)
                return type;
        }
        return NO;
    }

    public static final BRMSYesNo lookup(String value) {
        for (BRMSYesNo type : BRMSYesNo.values()) {
            if (type.value.equals(value))
                return type;
        }
        return NO;
    }

    public String value(){
        return value;
    }

    public boolean boolValue(){
        return boolValue;
    }
}
