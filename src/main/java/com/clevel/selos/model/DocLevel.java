package com.clevel.selos.model;

public enum DocLevel {
    NA(0), APP_LEVEL(1), CUS_LEVEL(2);

    private final int value;

    private DocLevel(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
