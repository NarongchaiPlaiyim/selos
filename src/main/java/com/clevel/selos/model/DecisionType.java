package com.clevel.selos.model;

public enum DecisionType {

    NO_DECISION(0), APPROVED(1), REJECTED(2);
    private final int value;

    private DecisionType(int value){
        this.value = value;
    }

    public int value() {
        return value;
    }


}
