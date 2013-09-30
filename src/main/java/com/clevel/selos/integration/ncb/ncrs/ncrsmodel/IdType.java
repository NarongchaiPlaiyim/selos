package com.clevel.selos.integration.ncb.ncrs.ncrsmodel;

public enum IdType{
    CITIZEN("01"), PASSPORT("07");

    private String value;

    private IdType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
