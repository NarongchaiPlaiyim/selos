package com.clevel.selos.model;

public enum Gender {
    MALE(1),FEMALE(2);



    int value;

    Gender(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
