package com.clevel.selos.model;

public enum RadioValue {
    NOT_SELECTED(-1),
    NO(0),
    YES(1),
    PASS(2),
    FAIL(3),
    NA(4);

    int value;

    RadioValue(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
