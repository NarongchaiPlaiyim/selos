package com.clevel.selos.model;

public enum Screen {
    //Basic Info
    BASIC_INFO(3001),

    //Customer Info
    CUSTOMER_INFO_SUMMARY(4001),
    CUSTOMER_INFO_INDIVIDUAL(4002),
    CUSTOMER_INFO_JURISTIC(4003);

    int value;

    Screen(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
