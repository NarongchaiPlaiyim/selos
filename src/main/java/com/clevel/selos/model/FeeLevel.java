package com.clevel.selos.model;

public enum FeeLevel {
    NA(0), APP_LEVEL(1), CREDIT_LEVEL(2);

    private final int value;

    private FeeLevel(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
