package com.clevel.selos.model;

public enum StepValue {
    PRESCREEN_INITIAL(1001),
    PRESCREEN_CHECKER(1002),
    PRESCREEN_MAKER(1003),
    FULLAPP_BDM_SSO(2001);

    int value;

    StepValue(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
