package com.clevel.selos.model;

public enum ProposeType {

    P(1), A(2);
    private final int value;

    ProposeType(int value) {
        this.value = value;
    }

    public int value(){
        return value;
    }
}
