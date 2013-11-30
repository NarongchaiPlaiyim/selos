package com.clevel.selos.model;

public enum BaseRateConfig {
    MLR(1), MOR(2), MRR(3);
    int value;

    BaseRateConfig(int value) {
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
