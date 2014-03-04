package com.clevel.selos.model;

public enum MortgageCategory {
    NA(0), REDEEM(1), INBOUND(2);

    private final int value;

    MortgageCategory(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
