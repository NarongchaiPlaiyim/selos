package com.clevel.selos.ws;

public enum WSResponse {
    SUCCESS(0), MANDATORY_REQUIRED(1), DUPLICATE_CA(2), VALIDATION_FAILED(3),DB_EXCEPTION(10),BPM_EXCEPTION(11),SYSTEM_EXCEPTION(12),FAILED(13);
    int code;

    WSResponse(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}