package com.clevel.selos.integration;

public enum IntegrationResult {
    SUCCESS(1),FAILED(2);
    int value;

    IntegrationResult(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
