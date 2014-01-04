package com.clevel.selos.integration.bpm.model;

public enum OrderType {
    DESCENDING("desc"),
    ASCENDING("asc");

    String value;

    OrderType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
