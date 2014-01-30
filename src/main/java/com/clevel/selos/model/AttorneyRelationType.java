package com.clevel.selos.model;

public enum AttorneyRelationType {
    NA(0), BORROWER(1), OTHERS(2);

    private int value;

    private AttorneyRelationType(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
