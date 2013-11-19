package com.clevel.selos.model;


/**
 * An Enum for mapping with Exposure Method in MST_DWHPRODUCT_FORMULA and MST_PRODUCT_FORMULA
 * NOT_CALCULATE = 0
 * LIMIT = 1
 * PCE_LIMIT = 2 : (Limit * %PCE)
 */
public enum ExposureMethod {

    NOT_CALCULATE(0), LIMIT(1), PCE_LIMIT(2), OUTSTANDING(3);
    int value;

    ExposureMethod(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
