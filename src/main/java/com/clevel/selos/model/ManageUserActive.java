package com.clevel.selos.model;

public enum ManageUserActive {
    ACTIVE(1),INACTIVE(0);

    int value;

    ManageUserActive(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
