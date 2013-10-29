package com.clevel.selos.model;

public enum BorrowerType {
    INDIVIDUAL(1), JURISTIC(2);
    int value;

    BorrowerType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
