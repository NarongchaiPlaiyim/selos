package com.clevel.selos.model;

public enum ProposeType {

    NA(0), P(1), A(2), BOTH(3), R(4);
    private final int value;

    ProposeType(int value) {
        this.value = value;
    }

    public int value(){
        return value;
    }
}
