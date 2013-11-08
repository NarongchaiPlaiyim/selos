package com.clevel.selos.model;

public enum RoleUser {
    BDM(102),UW(107);

    private int value;

    RoleUser(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
