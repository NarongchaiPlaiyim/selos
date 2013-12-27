package com.clevel.selos.integration.bpm.model;

public enum RoleName {
    BDM("bdm"),
    UW("uw");

    String value;

    RoleName(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
