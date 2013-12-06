package com.clevel.selos.model;

public enum RoleValue {
    BDM(102),
    UW(107);

    int id;

    RoleValue(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }
}
