package com.clevel.selos.model;

public enum RadioValue {
    NOT_SELECTED(0),
    NO(1),
    YES(2),
    PASS(3),
    FAIL(4),
    NA(5);

    int value;

    RadioValue(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
