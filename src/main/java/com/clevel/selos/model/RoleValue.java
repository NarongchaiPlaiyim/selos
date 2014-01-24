package com.clevel.selos.model;

public enum RoleValue {
    BDM(102),
    UW(107),
    ABDM(101),
    ZM(103);

    int id;

    RoleValue(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }
}
