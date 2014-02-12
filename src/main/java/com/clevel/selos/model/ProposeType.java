package com.clevel.selos.model;

public enum ProposeType {

    P(1,"P"), A(2,"A");
    int value;
    String type;

    ProposeType(int value, String type) {
        this.value = value;
        this.type = type;
    }

    public int value(){
        return value;
    }

    public String type(){
        return type;
    }
}
