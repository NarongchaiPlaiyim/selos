package com.clevel.selos.model;

public enum QualitativeClass {

    P(1), SM(2), SS(3), D(4), DL(5);
    private int value;

    QualitativeClass(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int value() {
        return this.value;
    }
}
