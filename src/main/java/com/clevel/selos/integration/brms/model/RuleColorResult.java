package com.clevel.selos.integration.brms.model;

public enum RuleColorResult {
    GREEN(1, "G"), YELLOW(2, "Y"), RED(3, "R");
    int value;
    private String persistValue;

    RuleColorResult(int value, String persistValue) {
        this.value = value;
        this.persistValue = persistValue;
    }

    public int value() {
        return this.value;
    }

    public String persistValue() {
        return persistValue;
    }
}