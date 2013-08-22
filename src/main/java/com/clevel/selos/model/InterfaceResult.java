package com.clevel.selos.model;

public enum InterfaceResult {
    SUCCESS(1),FAILED(2);
    int value;

    InterfaceResult(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
