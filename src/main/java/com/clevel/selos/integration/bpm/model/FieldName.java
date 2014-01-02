package com.clevel.selos.integration.bpm.model;

public enum FieldName {
    APPNUMBER("APPNUMBER"),
    RECEIVEDTIME("RECEIVEDTIME");

    String value;

    FieldName(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
