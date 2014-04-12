package com.clevel.selos.model;

public enum ProductGroupValue {
    TMB_SME_SMART_BIZ(1),
    F_CASH(2),
    OD_NO_ASSET(3),
    RETENTION(4),
    QUICK_LOAN(5);

    int id;

    ProductGroupValue(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }
}
