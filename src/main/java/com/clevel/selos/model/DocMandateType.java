package com.clevel.selos.model;

public enum DocMandateType {
    MANDATE(0), OPTIONAL(1), OTHER(2);

    private final int value;

    private DocMandateType(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
