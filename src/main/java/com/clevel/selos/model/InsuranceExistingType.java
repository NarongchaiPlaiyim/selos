package com.clevel.selos.model;

public enum InsuranceExistingType {
    NA(0), NO(1), YES(2), ADD_VALUE(3);

    private int value;

    private InsuranceExistingType(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
