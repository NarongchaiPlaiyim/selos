package com.clevel.selos.model;

public enum BAPAType {
    NA,
    BA,
    PA;

    public int value() {
        return ordinal();
    }
    public static final BAPAType lookup(int value) {
        for (BAPAType type : BAPAType.values()) {
            if (type.ordinal() == value)
                return type;
        }
        return NA;
    }

    public static final BAPAType[] displayList() {
        return new BAPAType[] {
                BA,PA
        };
    }
}
