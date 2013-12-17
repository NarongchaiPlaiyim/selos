package com.clevel.selos.model;

public enum RequestTypes {
    NEW(1),CHANGE(2);
    int value;

    RequestTypes(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
