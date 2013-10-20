package com.clevel.selos.model;

public enum CreditRelationType {

    BORROWER(1), RELATED(2);
    int value;

    CreditRelationType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
