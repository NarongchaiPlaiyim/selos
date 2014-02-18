package com.clevel.selos.model;

public enum BAPaymentMethodValue {
	NA(0,"NA"),
    TOPUP(1,"Top Up BA"),
    DIRECT(2,"Directly Pay by Customer");

    int value;
    String shortName;

    private BAPaymentMethodValue(int value, String shortName) {
        this.value = value;
        this.shortName = shortName;
    }

    public int value() {
        return this.value;
    }

    public String shortName() {
        return this.shortName;
    }
}
