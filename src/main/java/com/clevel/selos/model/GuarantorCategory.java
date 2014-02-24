package com.clevel.selos.model;

public enum GuarantorCategory {
    NA(0), INDIVIDUAL(1), JURISTIC(2), TCG(3);
    private final int value;

    GuarantorCategory(int value) {
        this.value = value;
    }

    public int value(){
        return value;
    }
}
