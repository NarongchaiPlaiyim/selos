package com.clevel.selos.model;

public enum CreditCategory {
    COMMERCIAL(1), RETAIL(2), RLOS_APP_IN(3);
    int value;

    CreditCategory(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
