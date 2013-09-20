package com.clevel.selos.model.view;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 16/9/2556
 * Time: 9:59 น.
 * To change this template use File | Settings | File Templates.
 */
public class MoneyPaymentView {
    private BigDecimal moneyPayment;


    public MoneyPaymentView() {
    }

    public BigDecimal getMoneyPayment() {
        return moneyPayment;
    }

    public void setMoneyPayment(BigDecimal moneyPayment) {
        this.moneyPayment = moneyPayment;
    }
}
