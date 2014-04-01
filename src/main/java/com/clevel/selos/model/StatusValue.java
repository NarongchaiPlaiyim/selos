package com.clevel.selos.model;

public enum StatusValue {
    REVIEW_CA(20005);

    int value;

    StatusValue(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
