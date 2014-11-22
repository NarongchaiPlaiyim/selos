package com.clevel.selos.model;

public enum ActionAudit {
    ON_CREATION("On Creation"),
    ON_SAVE("On Save"),
    ON_CANCEL("On Cancel"),
    ON_ADD("On Add"),
    ON_EDIT("On Edit"),
    ON_DELETE("On Delete"),
    ON_ACTION("On Action");

    String value;

    ActionAudit(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
