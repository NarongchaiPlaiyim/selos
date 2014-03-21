package com.clevel.selos.model;

public enum CaseRequestTypes {
    NEW_CASE(1),APPEAL_CASE(2),RESUBMIT_CASE(3);

    int value;

    CaseRequestTypes(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public int getValue() {
        return this.value;
    }
}
