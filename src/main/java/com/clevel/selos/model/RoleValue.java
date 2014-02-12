package com.clevel.selos.model;

public enum RoleValue {
    ABDM(101),
    BDM(102),
    ZM(103),
    RGM(104),
    UW(107);

    int id;

    RoleValue(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }
}
