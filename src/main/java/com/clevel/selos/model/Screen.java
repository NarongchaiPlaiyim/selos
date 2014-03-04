package com.clevel.selos.model;

public enum Screen {
    //PreScreen
    PRESCREEN_INITIAL(2001),
    PRESCREEN_CHECKER(2002),
    PRESCREEN_MAKER(2003),
    PRESCREEN_RESULT(2004),

    //Full Application
    //Basic Info
    BASIC_INFO(3001),

    //Customer Info
    CUSTOMER_INFO_SUMMARY(3002),
    CUSTOMER_INFO_INDIVIDUAL(3003),
    CUSTOMER_INFO_JURISTIC(3004);

    int value;

    Screen(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
