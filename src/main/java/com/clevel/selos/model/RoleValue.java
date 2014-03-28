package com.clevel.selos.model;

public enum RoleValue {
    ABDM(101),
    BDM(102),
    ZM(103),
    RGM(104),
    GH(105),
    CSSO(106),
    UW(107),
    AAD_ADMIN(108),
    AAD_COMITTEE(109);

    int id;

    RoleValue(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }
}
