package com.clevel.selos.model;

/**
 * An Enum for mapping with Normal/Prime Customer in MST_DWHPRODUCT_FORMULA and MST_PRODUCT_FORMULA table
 * Also, to display the Normal/Prime onto the screen.
 * NOT_SELECTED = 0
 * NORMAL = 1
 * PRIME = 2
 */
public enum CreditCustomerType {

    NOT_SELECTED(0, "", ""), NORMAL(1, "N", "Normal"), PRIME(2, "P", "Prime");
    private final int value;
    private final String brmscode;
    private final String shortName;

    CreditCustomerType(int value, String brmscode, String shortName) {
        this.value = value;
        this.brmscode = brmscode;
        this.shortName = shortName;
    }

    public int value() {
        return this.value;
    }

    public String brmsCode(){
        return this.brmscode;
    }

    public String shortName() {
        return this.shortName;
    }

    public static final CreditCustomerType lookup(int value) {
        for (CreditCustomerType creditCustomerType : CreditCustomerType.values()) {
            if (creditCustomerType.ordinal() == value)
                return creditCustomerType;
        }
        return NOT_SELECTED;
    }
}
