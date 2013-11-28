package com.clevel.selos.model;

public enum BaseRateConfig {
    MRR(1);
    int value;

    BaseRateConfig(int value) {
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
