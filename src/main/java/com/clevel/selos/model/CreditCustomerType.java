package com.clevel.selos.model;

/**
 * An Enum for mapping with Normal/Prime Customer in MST_DWHPRODUCT_FORMULA and MST_PRODUCT_FORMULA table
 * Also, to display the Normal/Prime onto the screen.
 * NOT_SELECTED = 0
 * NORMAL = 1
 * PRIME = 2
 */
public enum CreditCustomerType {

    NOT_SELECTED(0, ""), NORMAL(1, "N"), PRIME(2, "P");
    private final int value;
    private final String brmscode;

    CreditCustomerType(int value, String brmscode) {
        this.value = value;
        this.brmscode = brmscode;
    }

    public int value() {
        return this.value;
    }

    public String brmsCode(){
        return this.brmscode;
    }

    public static final CreditCustomerType lookup(int value) {
        for (CreditCustomerType creditCustomerType : CreditCustomerType.values()) {
            if (creditCustomerType.ordinal() == value)
                return creditCustomerType;
        }
        return NOT_SELECTED;
    }
}
