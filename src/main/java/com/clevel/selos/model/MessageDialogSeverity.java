package com.clevel.selos.model;

public enum MessageDialogSeverity {
    ALERT("alert"), INFO("info");

    private final String severity;

    private MessageDialogSeverity(String severity) {
        this.severity = severity;
    }

    public String severity() {
        return this.severity;
    }
}
