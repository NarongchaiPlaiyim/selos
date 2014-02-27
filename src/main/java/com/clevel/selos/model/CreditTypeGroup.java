package com.clevel.selos.model;

public enum CreditTypeGroup {
    LOAN(1),
    OD(2),
    LG(3),
    ACCEPTANCE(4),
    AVAL(5),
    LC(6),
    FX(7),
    CARD(8),
    CASH_IN(9),
    OTHER(10);

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
