package com.clevel.selos.model;

public enum RequestAppraisalValue {
    NOT_REQUEST(0),
    READY_FOR_REQUEST(1),
    REQUESTED(2);

    int value;

    RequestAppraisalValue(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
