package com.clevel.selos.model;

public enum StatusValue {
    REVIEW_CA(20005),
    CANCEL_CA(90001),
    REJECT_UW1(90002),
    REJECT_UW2(90007),
    REJECT_CA(90004);

    int value;

    StatusValue(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
