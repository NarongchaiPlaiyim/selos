package com.clevel.selos.model;

/**
 * An Enum for mapping with Normal/Prime Customer in MST_DWHPRODUCT_FORMULA and MST_PRODUCT_FORMULA table
 * Also, to display the Normal/Prime onto the screen.
 * NOT_SELECTED = 0
 * NORMAL = 1
 * PRIME = 2
 */
public enum CreditCustomerType {

    NOT_SELECTED(0), NORMAL(1), PRIME(2);
    int value;

    CreditCustomerType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
