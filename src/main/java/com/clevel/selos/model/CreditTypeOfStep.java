package com.clevel.selos.model;

public enum CreditTypeOfStep {
    NEW("N"), EXISTING("E");

    private String type;

    private CreditTypeOfStep(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
