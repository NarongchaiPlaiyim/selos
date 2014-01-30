package com.clevel.selos.model;


public enum  BaPaType {
    BA(1),PA(2);

    int value;

    BaPaType(int value) {
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
