package com.clevel.selos.model;

import java.io.Serializable;

public enum ApprovalType implements Serializable {
    CA_APPROVAL(1),
    PRICING_APPROVAL(2);

    private final int value;

    private ApprovalType(int value){
        this.value = value;
    }

    public int value() {
        return value;
    }
}
