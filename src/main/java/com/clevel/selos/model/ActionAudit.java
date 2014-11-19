package com.clevel.selos.model;

public enum ActionAudit {
    ON_CREATION("On Creation"),
    ON_SAVE("On Save"),
    ON_CANCEL("On Cancel");

    String value;

    ActionAudit(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
