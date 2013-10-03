package com.clevel.selos.integration.ncb.ncrs.ncrsmodel;

public enum TitleName {
    Mr("01"), Mrs("02"), Miss("03");

    private String value;

    private TitleName(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
