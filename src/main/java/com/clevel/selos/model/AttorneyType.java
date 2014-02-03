package com.clevel.selos.model;

public enum AttorneyType {
    NA(0), ATTORNEY_IN_RIGHT(1), POWER_OF_ATTORNEY(2);

    private int value;

    private AttorneyType(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
