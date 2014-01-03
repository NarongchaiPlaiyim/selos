package com.clevel.selos.model;

public enum RequestTypes {
    NEW(2),CHANGE(1);
    int value;

    RequestTypes(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
