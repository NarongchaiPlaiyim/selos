package com.clevel.selos.integration;

public enum IntegrationStatus {
    SUCCESS(0),ADDED(1),SENDING(2),FAILED(3);
    int value;

    IntegrationStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
