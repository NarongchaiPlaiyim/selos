package com.clevel.selos.model;

public enum PricingDOAValue {
    ZM_DOA(1),
    RGM_DOA(2),
    GH_DOA(3),
    CSSO_DOA(4);

    int value;

    PricingDOAValue(int value){
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
