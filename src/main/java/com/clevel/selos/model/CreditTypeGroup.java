package com.clevel.selos.model;

public enum CreditTypeGroup {
    LOAN(0),
    OD(1),
    LG(2),
    ACCEPTANCE(3),
    AVAL(4),
    LC(5),
    FX(6),
    CARD(7),
    CASH_IN(8),
    OTHER(9);

    int value;

    CreditTypeGroup(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static CreditTypeGroup getCreditTypeGroup(int value) {
        for (CreditTypeGroup creditTypeGroup : values()) {
            if (creditTypeGroup.value == value) {
                return creditTypeGroup;
            }
        }
        return null;
    }
}
