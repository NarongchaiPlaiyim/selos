package com.clevel.selos.model;

public enum RelationValue {
    BORROWER(1),
    GUARANTOR(2),
    DIRECTLY_RELATED(3),
    INDIRECTLY_RELATED(4);

    int value;

    private RelationValue(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static final RelationValue lookup(int value){
        for(RelationValue relationValue : RelationValue.values()){
            if(relationValue.value == value)
                return relationValue;
        }
        return INDIRECTLY_RELATED;
    }
}
