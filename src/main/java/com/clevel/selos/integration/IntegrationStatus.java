package com.clevel.selos.integration;

public enum IntegrationStatus {
    SUCCESS(0),SENDING(1),FAILED(2);
    int value;

    IntegrationStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
