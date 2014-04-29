package com.clevel.selos.model;

public enum ReasonTypeValue {
    REJECT_REASON(1),
    RETURN_REASON(2),
    CANCEL_REASON(3),
    QUALITATIVE_LEVEL_REASON(4),
    APPEAL_REASON(5),
    DEVIATE_REASON(6),
    OVER_SLA_REASON(7),
    PENDING_REASON(8);

    int value;

    private ReasonTypeValue(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
