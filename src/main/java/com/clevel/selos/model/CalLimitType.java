package com.clevel.selos.model;

public enum CalLimitType {
    LIMIT(1), OUTSTANDING(2), PCE(3);
    int value;

    CalLimitType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static CalLimitType getCalLimitType(int value) {
        for (CalLimitType calLimitType : values()) {
            if (calLimitType.value == value) {
                return calLimitType;
            }
        }
        return null;
    }
}
