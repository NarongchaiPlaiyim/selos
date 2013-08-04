package com.clevel.selos.model;

public enum Sex {
    MALE(1),FEMALE(2);
    int value;

    Sex(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
