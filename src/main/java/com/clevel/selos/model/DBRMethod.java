package com.clevel.selos.model;

/**
 * Created with IntelliJ IDEA.
 * User: pinthip
 * Date: 11/18/13 AD
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */
/**
 * An Enum for mapping with Exposure Method in MST_DWHPRODUCT_FORMULA and MST_PRODUCT_FORMULA
 * NOT_CALCULATE = 0
 * INSTALLMENT = 1 (Using Installment to calculate)
 * INT_YEAR = 2 : (Using Interest to calculate)
 */
public enum DBRMethod {

    NOT_CALCULATE(0), INSTALLMENT(1), INT_YEAR(2);
    int value;

    DBRMethod(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
