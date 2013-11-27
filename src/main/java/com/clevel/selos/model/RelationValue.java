package com.clevel.selos.model;

public enum RelationValue {
    BORROWER(1),
    GUARANTOR(2),
    DIRECTLY_RELATED(3),
    INDIRECTLY_RELATED(4);

    int value;

    RelationValue(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
